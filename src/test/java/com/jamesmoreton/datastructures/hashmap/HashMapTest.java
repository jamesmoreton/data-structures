package com.jamesmoreton.datastructures.hashmap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HashMapTest {

  private HashMap<String, String> hashMap;

  @BeforeEach
  void setUp() {
    hashMap = new HashMap<>();
  }

  @Test
  void getBeforeAnyPutsReturnsNull() {
    assertThat(hashMap.get("key1")).isNull();
  }

  @Test
  void getNonExistentKeyReturnsNull() {
    hashMap.put("key1", "value1");
    assertThat(hashMap.get("key2")).isNull();
  }

  @Test
  void canPutAndGetValue() {
    hashMap.put("key1", "value1");
    assertThat(hashMap.get("key1")).isEqualTo("value1");
  }

  @Test
  void canPutAndGetValueOnCollision() {
    hashMap.put("key10", "value1");
    hashMap.put("key47", "value2");
    assertThat(hashMap.get("key10")).isEqualTo("value1");
    assertThat(hashMap.get("key47")).isEqualTo("value2");
  }

  @Test
  void canUpdateValueForKey() {
    hashMap.put("key1", "value1");
    hashMap.put("key1", "value2");
    assertThat(hashMap.get("key1")).isEqualTo("value2");
  }

  @Test
  void canUpdateValueForKeyOnCollision() {
    hashMap.put("key10", "value1");
    hashMap.put("key47", "value2");
    hashMap.put("key47", "value3");
    assertThat(hashMap.get("key10")).isEqualTo("value1");
    assertThat(hashMap.get("key47")).isEqualTo("value3");
  }

  @Test
  void getNonExistentKeyOnCollisionReturnsNull() {
    hashMap.put("key10", "value1");
    assertThat(hashMap.get("key47")).isNull();
  }

  @Test
  void removeBeforeAnyPutsDoesNothing() {
    hashMap.remove("key1");
  }

  @Test
  void removeNonExistentKeyDoesNothing() {
    hashMap.put("key1", "value1");
    hashMap.remove("key2");
    assertThat(hashMap.get("key1")).isEqualTo("value1");
  }

  @Test
  void canRemoveKey() {
    hashMap.put("key1", "value1");
    hashMap.remove("key1");
    assertThat(hashMap.get("key1")).isNull();
  }

  @Test
  void canRemoveKeyOnCollisionFirst() {
    hashMap.put("key10", "value1");
    hashMap.put("key47", "value2");
    hashMap.remove("key10");
    assertThat(hashMap.get("key10")).isNull();
    assertThat(hashMap.get("key47")).isEqualTo("value2");
  }

  @Test
  void canRemoveKeyOnCollisionLast() {
    hashMap.put("key10", "value1");
    hashMap.put("key47", "value2");
    hashMap.remove("key47");
    assertThat(hashMap.get("key10")).isEqualTo("value1");
    assertThat(hashMap.get("key47")).isNull();
  }

  @Test
  void canRemoveKeyOnCollisionMiddle() {
    hashMap.put("key10", "value1");
    hashMap.put("key47", "value2");
    hashMap.put("key133", "value3");
    hashMap.remove("key47");
    assertThat(hashMap.get("key10")).isEqualTo("value1");
    assertThat(hashMap.get("key47")).isNull();
    assertThat(hashMap.get("key133")).isEqualTo("value3");
  }

  @Test
  void removeNonExistentKeyOnCollisionDoesNothing() {
    hashMap.put("key10", "value1");
    hashMap.remove("key47");
    assertThat(hashMap.get("key10")).isEqualTo("value1");
  }
}