package com.jamesmoreton.datastructures.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements methods of {@link Cache} with expiration in O(1) using a {@link DelayQueue}.
 */
public class InMemoryCache<K, V> implements Cache<K, V> {

  private static class Item<K> implements Delayed {

    private final K key;
    private final long expiryTime;

    private Item(K key, long expiryTime) {
      this.key = key;
      this.expiryTime = expiryTime;
    }

    public K getKey() {
      return key;
    }

    @Override
    public long getDelay(TimeUnit unit) {
      return unit.convert(expiryTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(Delayed o) {
      return Long.compare(expiryTime, ((Item<K>) o).expiryTime);
    }
  }

  private static final Logger logger = LoggerFactory.getLogger(InMemoryCache.class);
  private static final int UNSET_INT = -1;

  private final long expiryPeriod;
  private final ConcurrentHashMap<K, V> cache;
  private final DelayQueue<Item<K>> cleanUpQueue;

  private InMemoryCache(InMemoryCacheBuilder<? super K, ? super V> builder) {
    this.expiryPeriod = builder.expiryPeriod == null ? UNSET_INT : builder.expiryPeriod;
    this.cache = new ConcurrentHashMap<>();
    this.cleanUpQueue = new DelayQueue<>();

    if (expiryPeriod == UNSET_INT) {
      return;
    }

    logger.info("Starting cache cleaner for expiry period {}ms", expiryPeriod);
    Thread cleanerThread = new Thread(() -> {
      while (!Thread.currentThread().isInterrupted()) {
        try {
          Item<K> item = cleanUpQueue.take();
          cache.remove(item.getKey());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    });
    cleanerThread.setDaemon(true);
    cleanerThread.start();
  }

  public static class InMemoryCacheBuilder<K, V> {

    private Long expiryPeriod;

    private InMemoryCacheBuilder() {
    }

    public static InMemoryCacheBuilder<Object, Object> newBuilder() {
      return new InMemoryCacheBuilder<>();
    }

    public InMemoryCacheBuilder<K, V> withExpiry(long expiryPeriod) {
      this.expiryPeriod = expiryPeriod;
      return this;
    }

    public <K1 extends K, V1 extends V> InMemoryCache<K1, V1> build() {
      return new InMemoryCache<>(this);
    }
  }

  @Override
  public void add(K key, V value) {
    if (key == null) {
      return;
    }
    if (value == null) {
      cache.remove(key);
      return;
    }
    cache.put(key, value);
    if (expiryPeriod != UNSET_INT) {
      final long expiryTime = System.currentTimeMillis() + expiryPeriod;
      cleanUpQueue.put(new Item<>(key, expiryTime));
    }
  }

  @Override
  public void remove(K key) {
    cache.remove(key);
  }

  @Override
  public V get(K key) {
    return cache.get(key);
  }

  @Override
  public void clear() {
    cache.clear();
  }

  @Override
  public long size() {
    return cache.size();
  }
}
