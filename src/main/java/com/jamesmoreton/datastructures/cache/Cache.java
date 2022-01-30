package com.jamesmoreton.datastructures.cache;

public interface Cache<K, V> {

  void add(K key, V value);

  void remove(K key);

  V get(K key);

  void clear();

  long size();
}
