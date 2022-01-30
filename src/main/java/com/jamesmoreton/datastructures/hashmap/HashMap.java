package com.jamesmoreton.datastructures.hashmap;

/**
 * Implements methods of {@link Map} in O(1) using an array-based linked list.
 *
 * <p>If the number of collisions is high, worst case runtime is O(n), where n is the number of
 * keys.</p>
 */
public class HashMap<K, V> implements Map<K, V> {

  private static class Node<K, V> {

    private final K key;
    private V value;
    private Node<K, V> next;

    private Node(K key, V value) {
      this.key = key;
      this.value = value;
      this.next = null;
    }
  }

  private static final int BUCKETS = 100;

  private Node<K, V>[] table;

  @SuppressWarnings("unchecked")
  @Override
  public void put(K key, V value) {
    if (table == null) {
      table = (Node<K, V>[]) new Node[BUCKETS];
    }

    final int hash = hashCode(key);
    if (table[hash] == null) {
      table[hash] = new Node<>(key, value);
      return;
    }

    putVal(key, value, table[hash]);
  }

  private void putVal(K key, V value, Node<K, V> node) {
    if (node.key == key) {
      node.value = value;
      return;
    }

    if (node.next == null) {
      node.next = new Node<>(key, value);
      return;
    }

    putVal(key, value, node.next);
  }

  @Override
  public V get(K key) {
    if (table == null) {
      return null;
    }

    final int hash = hashCode(key);
    if (table[hash] == null) {
      return null;
    }

    return getVal(key, table[hash]);
  }

  private V getVal(K key, Node<K, V> node) {
    if (key == node.key) {
      return node.value;
    }

    if (node.next == null) {
      return null;
    }

    return getVal(key, node.next);
  }

  @Override
  public void remove(K key) {
    if (table == null) {
      return;
    }

    final int hash = hashCode(key);
    if (table[hash] == null) {
      return;
    }

    removeVal(key, null, table[hash]);
  }

  private void removeVal(K key, Node<K, V> prev, Node<K, V> node) {
    if (key == node.key) {
      if (prev == null) {
        table[hashCode(key)] = node.next == null ? null : node.next;
      } else {
        prev.next = node.next == null ? null : node.next;
      }
      return;
    }

    if (node.next == null) {
      return;
    }

    removeVal(key, node, node.next);
  }

  private int hashCode(K key) {
    final int hash = key.hashCode() % BUCKETS;
    return hash < 0 ? -hash : hash;
  }
}
