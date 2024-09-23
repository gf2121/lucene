package org.apache.lucene.codecs.lucene90.blocktree;

import java.io.IOException;
import org.apache.lucene.store.IndexInput;

class TrieReader {

  private static final int META_BYTES = 3;
  private static final long NO_OUTPUT = -1;

  static class Node {
    Node parent;
    long fp;
    long outputFp;
    int label;
    boolean isLeaf;
    Trie.PositionStrategy childrenStrategy;
    int positionBytes;
    int minChildrenLabel;
    int childrenCodesBytes;

    IndexInput output(TrieReader reader) throws IOException {
      if (outputFp == NO_OUTPUT) {
        return null;
      }
      reader.outputsIn.seek(outputFp);
      return reader.outputsIn;
    }
  }

  private final IndexInput arcsIn;
  private final IndexInput outputsIn;
  private final Node root;

  TrieReader(IndexInput meta, IndexInput index) throws IOException {
    long arcInStart = meta.readVLong();
    long rootFP = meta.readVLong();
    long arcInEnd = meta.readVLong();
    arcsIn = index.slice("trie arcs", arcInStart, arcInEnd - arcInStart);
    long outputEnd = meta.readVLong();
    outputsIn = index.slice("outputs", arcInEnd, outputEnd - arcInEnd);
    root = new Node();
    load(root, rootFP, null);
  }

  private void load(Node node, long code, Node parent) throws IOException {
    System.out.println(code);
    node.parent = parent;
    if ((code & 0x1) == 0) {
      node.fp = code >>> 1;
      node.outputFp = NO_OUTPUT;
    } else {
      long outputFP = code >>> 1;
      outputsIn.seek(outputFP);
      node.fp = outputsIn.readVLong();
      node.outputFp = outputsIn.getFilePointer();
      if (node.fp == 0) {
        node.isLeaf = true;
        return;
      }
    }
    arcsIn.seek(node.fp);
    int sign = arcsIn.readShort() & 0xFFFF;
    node.childrenStrategy = Trie.PositionStrategy.byCode(sign >>> 14);
    node.positionBytes = (sign >>> 8) & 0x3F;
    node.minChildrenLabel = sign & 0xFF;
    node.childrenCodesBytes = arcsIn.readByte() & 0xFF;
    assert arcsIn.getFilePointer() == node.fp + META_BYTES;
  }

  boolean lookupChild(byte targetLabel, Node parent, Node child) throws IOException {
    if (parent.isLeaf) return false;
    long positionBytesFp = parent.fp + META_BYTES;
    arcsIn.seek(positionBytesFp);
    int position =
        parent.childrenStrategy.position(
            targetLabel, arcsIn, parent.positionBytes, parent.minChildrenLabel);
    System.out.println("label: " + targetLabel + " position: " + position);
    if (position < 0) {
      return false;
    }
    arcsIn.seek(
        positionBytesFp + parent.positionBytes + (long) parent.childrenCodesBytes * position);
    long code = readNBytesLong(arcsIn, parent.childrenCodesBytes);
    load(child, code, parent);
    return true;
  }

  public Node getRoot() {
    return root;
  }

  private static long readNBytesLong(IndexInput in, int n) throws IOException {
    switch (n) {
      case 1:
        return in.readByte() & 0xFFL;
      case 2:
        return in.readShort() & 0xFFFFL;
      case 3:
        {
          long b = in.readByte() & 0xFFL;
          long s = in.readShort() & 0xFFFFL;
          return (s << 8) | b;
        }
      case 4:
        return in.readInt() & 0xFFFFFFFFL;
      case 5:
        {
          long b = in.readByte() & 0xFFL;
          long i = in.readInt() & 0xFFFFFFFFL;
          return (i << 8) | b;
        }
      case 6:
        {
          long s = in.readShort() & 0xFFFFL;
          long i = in.readInt() & 0xFFFFFFFFL;
          return (i << 16) | s;
        }
      case 7:
        {
          long b = in.readByte() & 0xFFL;
          long s = in.readShort() & 0xFFFFL;
          long i = in.readInt() & 0xFFFFFFFFL;
          return (i << 24) | (s << 8) | b;
        }
      case 8:
        return in.readLong();
      default:
        throw new IllegalArgumentException("illegal bytes num: " + n);
    }
  }
}
