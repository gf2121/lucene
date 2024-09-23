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
import java.util.Map;
import java.util.TreeMap;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.tests.util.LuceneTestCase;
import org.apache.lucene.util.BytesRef;

public class TestTrie extends LuceneTestCase {

  public void testTrie() {
    Map<BytesRef, BytesRef> actual = new TreeMap<>();
    Map<BytesRef, BytesRef> expected = new TreeMap<>();

    expected.put(new BytesRef(""), new BytesRef("emptyOutput"));
    Trie trie = new Trie(new BytesRef(""), new BytesRef("emptyOutput"));

    int n = random().nextInt(10000);
    for (int i = 0; i < n; i++) {
      byte[] k = new byte[random().nextInt(100)];
      byte[] v = new byte[random().nextInt(100)];
      random().nextBytes(k);
      random().nextBytes(v);
      BytesRef key = new BytesRef(k);
      BytesRef value = new BytesRef(v);
      expected.put(key, value);
      trie.putAll(new Trie(key, value));
    }
    trie.forEach(actual::put);
    assertEquals(expected, actual);
  }

  public void testSave() throws IOException {

    Trie trie = new Trie(new BytesRef(""), new BytesRef("emptyOutput"));

    //    int n = random().nextInt(10000);
    //    for (int i = 0; i < n; i++) {
    //      byte[] k = new byte[random().nextInt(100)];
    //      byte[] v = new byte[random().nextInt(100)];
    //      random().nextBytes(k);
    //      random().nextBytes(v);
    //      BytesRef key = new BytesRef(k);
    //      BytesRef value = new BytesRef(v);
    //      trie.putAll(new Trie(key, value));
    //    }

    trie.putAll(new Trie(new BytesRef("abc"), new BytesRef("1")));
    trie.putAll(new Trie(new BytesRef("abd"), new BytesRef("2")));
    trie.putAll(new Trie(new BytesRef("abf"), new BytesRef("3")));
    trie.putAll(new Trie(new BytesRef("abg"), new BytesRef("4")));

    try (Directory directory = newDirectory()) {
      try (IndexOutput index = directory.createOutput("index", IOContext.DEFAULT);
          IndexOutput meta = directory.createOutput("meta", IOContext.DEFAULT)) {
        trie.save(meta, index);
      }

      try (IndexInput indexIn = directory.openInput("index", IOContext.DEFAULT);
          IndexInput metaIn = directory.openInput("meta", IOContext.DEFAULT)) {
        TrieReader reader = new TrieReader(metaIn, indexIn);

        TrieReader.Node parent = reader.getRoot();
        TrieReader.Node child = reader.getRoot();
        assertFalse(reader.lookupChild(new BytesRef("b").bytes[0], parent, child));
        assertTrue(reader.lookupChild(new BytesRef("a").bytes[0], parent, child));
        parent = child;
        child = new TrieReader.Node();
        assertTrue(reader.lookupChild(new BytesRef("b").bytes[0], parent, child));
        assertNull(child.output(reader));
        parent = child;
        child = new TrieReader.Node();
        assertEquals(Trie.PositionStrategy.BITS, parent.childrenStrategy);
        assertTrue(reader.lookupChild(new BytesRef("c").bytes[0], parent, child));
        assertEquals(new BytesRef("1").bytes[0], child.output(reader).readByte() & 0xFF);
        assertTrue(reader.lookupChild(new BytesRef("d").bytes[0], parent, child));
        assertEquals(new BytesRef("2").bytes[0], child.output(reader).readByte() & 0xFF);
        assertTrue(reader.lookupChild(new BytesRef("f").bytes[0], parent, child));
        assertEquals(new BytesRef("3").bytes[0], child.output(reader).readByte() & 0xFF);
        assertTrue(reader.lookupChild(new BytesRef("g").bytes[0], parent, child));
        assertEquals(new BytesRef("4").bytes[0], child.output(reader).readByte() & 0xFF);
        assertFalse(reader.lookupChild(new BytesRef("h").bytes[0], parent, child));
      }
    }
  }
}
