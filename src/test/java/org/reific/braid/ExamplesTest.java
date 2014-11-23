package org.reific.braid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.reific.braid.lzw.LZW;

public class ExamplesTest {

  class Example {
    private final Braid value;

    public Example(Knot knot, String string) {
      this.value = Braids.newExampleBraid(knot, string);
    }

    public String getValue() {
      return value.get();
    }
  }

  @Test
  public void testBasicFunctionality() {
    final Knot knot = Knots.example();
    String string = new String("test");
    Braid braid = Braids.newExampleBraid(knot, string);
    assertEquals(string, braid.get());
    assertNotSame(string, braid.get());
  }

  @Test
  public void testSimpleCase() {
    final Knot knot = Knots.example();

    List<String> preCompressed = Arrays.asList(new String("test1"), new String("test2"));
    List<String> unCompressed =
        preCompressed.stream().map(s -> new Example(knot, s)).map(e -> e.getValue()).collect(Collectors.toList());

    assertEquals(preCompressed, unCompressed);

  }

  @Test
  public void testEncodeDecode() throws IOException {
    LZW lzw = new LZW("a", "b", "d", "n", "_");
    String startStr = "abababababababababababababababababab"; // "banana_bandana";

    ByteArrayOutputStream encodedByteStream = new ByteArrayOutputStream();
    lzw.encode(new StringReader(startStr), new DataOutputStream(encodedByteStream));

    ByteArrayInputStream forDecode = new ByteArrayInputStream(encodedByteStream.toByteArray());
    StringWriter decodedResult = new StringWriter();
    lzw.decode(new DataInputStream(forDecode), decodedResult);

    assertEquals(startStr, decodedResult.toString());
  }
}
