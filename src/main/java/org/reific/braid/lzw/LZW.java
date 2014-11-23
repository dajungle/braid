package org.reific.braid.lzw;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class LZW {

  private final String[] initialDictionaryEntries;

  public LZW(String... initialDictionaryEntries) {
    this.initialDictionaryEntries = initialDictionaryEntries;
  }

  public void encode(StringReader input, DataOutputStream out) throws IOException {
    EncodeDictionary dictionary = new EncodeDictionary(initialDictionaryEntries);
    String s = "";
    int in = input.read();
    while (in >= 0) {
      char ch = (char) in;
      String sAndCh = s + ch;
      if (dictionary.contains(sAndCh))
      {
        s = sAndCh;
      }
      else
      {
        out.writeInt(dictionary.codeOf(s));
        dictionary.add(sAndCh);
        s = new String(new char[] {ch});
      }
      in = input.read();
    }
    out.writeInt(dictionary.codeOf(s));
  }

  public void decode(DataInputStream in, StringWriter out) throws IOException {
    DecodeDictionary dictionary = new DecodeDictionary(initialDictionaryEntries);
    String entry = null;
    int prevCode = in.readInt();
    out.append(dictionary.lookup(prevCode));
    while (in.available() > 0)
    {
      int currCode = in.readInt();
      if (!dictionary.containsCode(currCode)) {
        entry = entry + entry.charAt(0);
      }
      else {
        entry = dictionary.lookup(currCode);
      }
      out.append(entry);
      dictionary.add(dictionary.lookup(prevCode) + entry.charAt(0));
      prevCode = currCode;
    }
  }
}
