package com.jamesmoreton.datastructures.cache;

import static org.assertj.core.api.Assertions.assertThat;

import com.jamesmoreton.datastructures.cache.InMemoryCache.InMemoryCacheBuilder;
import org.junit.jupiter.api.Test;

class InMemoryCacheTest {

  @Test
  void canAddAndGetFromCache() {
    InMemoryCache<String, String> cache = InMemoryCacheBuilder.newBuilder().build();
    cache.add("key1", "value1");
    assertThat(cache.get("key1")).isEqualTo("value1");
  }

  @Test
  void addNullKeyDoesNothing() {
    InMemoryCache<String, String> cache = InMemoryCacheBuilder.newBuilder().build();
    cache.add(null, "value1");
  }

  @Test
  void addNullValueRemovesKey() {
    InMemoryCache<String, String> cache = InMemoryCacheBuilder.newBuilder().build();
    cache.add("key1", "value1");
    cache.add("key1", null);
    assertThat(cache.get("key1")).isNull();
  }

  @Test
  void canRemoveFromCache() {
    InMemoryCache<String, String> cache = InMemoryCacheBuilder.newBuilder().build();
    cache.add("key1", "value1");
    cache.remove("key1");
    assertThat(cache.get("key1")).isNull();
  }

  @Test
  void canClearCache() {
    InMemoryCache<String, String> cache = InMemoryCacheBuilder.newBuilder().build();
    cache.add("key1", "value1");
    cache.clear();
    assertThat(cache.get("key1")).isNull();
  }

  @Test
  void canGetSizeOfCache() {
    InMemoryCache<String, String> cache = InMemoryCacheBuilder.newBuilder().build();
    cache.add("key1", "value1");
    assertThat(cache.size()).isOne();
  }

  @Test
  void canAddAndGetFromCacheBeforeExpiry() {
    InMemoryCache<String, String> cache = InMemoryCacheBuilder.newBuilder()
        .withExpiry(1000).build();
    cache.add("key1", "value1");
    assertThat(cache.get("key1")).isEqualTo("value1");
  }

  @Test
  void canAddAndNotGetFromCacheAfterExpiry() throws InterruptedException {
    InMemoryCache<String, String> cache = InMemoryCacheBuilder.newBuilder()
        .withExpiry(1).build();
    cache.add("key1", "value1");
    Thread.sleep(100);
    assertThat(cache.get("key1")).isNull();
  }
}