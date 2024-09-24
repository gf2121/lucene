/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.codecs.lucene90.blocktree;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.tests.util.LuceneTestCase;
import org.apache.lucene.util.ArrayUtil;
import org.apache.lucene.util.BytesRef;
import org.junit.Assert;

public class TestTrie extends LuceneTestCase {

  public void testTrie() {
    Map<BytesRef, BytesRef> actual = new TreeMap<>();
    Map<BytesRef, BytesRef> expected = new TreeMap<>();

    expected.put(new BytesRef(""), new BytesRef("emptyOutput"));
    Trie trie = new Trie(new BytesRef(""), new BytesRef("emptyOutput"));

    int n = random().nextInt(10000);
    for (int i = 0; i < n; i++) {
      BytesRef key = new BytesRef(randomBytes());
      BytesRef value = new BytesRef(randomBytes());
      expected.put(key, value);
      trie.putAll(new Trie(key, value));
    }
    trie.forEach(actual::put);
    assertEquals(expected, actual);
  }

  public void testTrieLookup() throws IOException {

    Map<BytesRef, BytesRef> expected = new TreeMap<>();

    expected.put(new BytesRef(""), new BytesRef("emptyOutput"));
    Trie trie = new Trie(new BytesRef(""), new BytesRef("emptyOutput"));

    int n = random().nextInt(100000);
    for (int i = 0; i < n; i++) {
      BytesRef key = new BytesRef(randomBytes());
      BytesRef value = new BytesRef(randomBytes());
      expected.put(key, value);
      trie.putAll(new Trie(key, value));
    }

    try (Directory directory = newDirectory()) {
      try (IndexOutput index = directory.createOutput("index", IOContext.DEFAULT);
           IndexOutput meta = directory.createOutput("meta", IOContext.DEFAULT)) {
        trie.save(meta, index);
      }

      try (IndexInput indexIn = directory.openInput("index", IOContext.DEFAULT);
           IndexInput metaIn = directory.openInput("meta", IOContext.DEFAULT)) {
        TrieReader reader = new TrieReader(metaIn, indexIn);

        for (var entry : expected.entrySet()) {
          assertLookup(reader, entry.getKey(), entry.getValue());
        }
      }
    }
  }

  private static byte[] randomBytes() {
    byte[] bytes = new byte[random().nextInt(256)];
    for (int i = 1; i < bytes.length; i++) {
      bytes[i] = (byte) random().nextInt(i);
    }
    return bytes;
  }

  private static void assertLookup(TrieReader reader, BytesRef term, BytesRef expected)
      throws IOException {
    TrieReader.Node parent = reader.getRoot();
    TrieReader.Node child = new TrieReader.Node();
    String[] chain = new String[term.length];
    for (int i = 0; i < term.length; i++) {
      boolean found = reader.lookupChild(term.bytes[i + term.offset], parent, child);
      chain[i] = parent.childrenStrategy.name();
      Assert.assertTrue(Arrays.toString(ArrayUtil.copyOfSubArray(chain, 0, i + 1)) + " look up failed.", found);
      parent = child;
      child = new TrieReader.Node();
    }
    IndexInput in = parent.output(reader);
    assertNotNull(in);
    byte[] bytes = new byte[expected.length];
    in.readBytes(bytes, 0, bytes.length);
    assertArrayEquals(
        ArrayUtil.copyOfSubArray(expected.bytes, expected.offset, expected.length), bytes);
  }
}
