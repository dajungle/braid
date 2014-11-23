package org.reific.braid.lzw;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class DecodeDictionary {
  private final AtomicInteger index = new AtomicInteger(0);
  Map<Integer, String> d = new HashMap<>();

  public DecodeDictionary(String... initialStrings) {
    Arrays.stream(initialStrings).forEach(this::add);
  }

  public void add(String s) {
    if (s == null) {
      throw new IllegalArgumentException("Can't decode null strings.");
    }
    d.put(index.getAndIncrement(), s);
  }

  public String stringOf(int code) {
    return d.get(code);
  }

  public boolean containsCode(int code) {
    return d.containsKey(code);
  }
}
