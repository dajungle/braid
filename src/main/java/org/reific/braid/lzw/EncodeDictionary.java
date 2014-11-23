package org.reific.braid.lzw;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class EncodeDictionary {
  private final AtomicInteger index = new AtomicInteger(0);
  Map<String, Integer> d = new HashMap<>();

  public EncodeDictionary(String... initialStrings) {
    Arrays.stream(initialStrings).forEach(this::add);
  }

  public void add(String s) {
    if (s == null) {
      throw new IllegalArgumentException("Can't encode null strings.");
    }
    d.put(s, index.getAndIncrement());
  }

  public int codeOf(String s) {
    return d.get(s);
  }

  public boolean contains(String s) {
    return d.containsKey(s);
  }
}
