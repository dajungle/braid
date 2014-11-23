package org.reific.braid.lzw;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class LZW {

  private final Charset outputCharset;

  private final String[] initialDictionaryEntries;

  public LZW(String... initialDictionaryEntries) {
    this(StandardCharsets.UTF_8, initialDictionaryEntries);
  }

  public LZW(Charset outputCharset, String... initialDictionaryEntries) {
    this.outputCharset = outputCharset;
    this.initialDictionaryEntries = initialDictionaryEntries;
  }

  public void encode(InputStream input, OutputStream out) throws IOException {
    EncodeDictionary dictionary = new EncodeDictionary(initialDictionaryEntries);
    String s = "";
    while (input.available() > 0) {
      char ch = (char) input.read();
      String sAndCh = s + ch;
      if (dictionary.contains(sAndCh))
      {
        s = sAndCh;
      }
      else
      {
        write(dictionary.codeOf(s), out);
        dictionary.add(sAndCh);
        s = new String(new char[] {ch});
      }
    }
    write(dictionary.codeOf(s), out);
  }

  private void write(int i, OutputStream out) throws IOException {
    out.write(String.valueOf(i).getBytes(outputCharset));
  }

  public void decode(InputStream in, OutputStream out) throws IOException {
    DecodeDictionary dictionary = new DecodeDictionary(initialDictionaryEntries);

    String entry = null;
    int prevCode = readCode(in);
    write(dictionary.stringOf(prevCode), out);
    while (in.available() > 0)
    {
      int currCode = readCode(in);
      if (!dictionary.containsCode(currCode)) {
        entry = entry + entry.charAt(0);
      }
      else {
        entry = dictionary.stringOf(currCode);
      }
      write(entry, out);
      dictionary.add(dictionary.stringOf(prevCode) + entry.charAt(0));
      prevCode = currCode;
    }
  }

  // TODO: there has to be a better way.
  private int readCode(InputStream in) throws NumberFormatException, IOException {
    return Integer.valueOf(new String(new char[] {(char) in.read()}));
  }

  private void write(String str, OutputStream out) throws IOException {
    out.write(str.getBytes(outputCharset));
  }
}
