package org.apache.lucene.codecs.lucene90.blocktree;

import java.io.IOException;
import java.util.stream.IntStream;

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
    if (parent.isLeaf) {
      return false;
    }

    int target = targetLabel & 0xFF;
    long positionBytesFp = parent.fp + META_BYTES;
    arcsIn.seek(positionBytesFp);
    int position;
    if (target <= parent.minChildrenLabel) {
      position = target == parent.minChildrenLabel ? 0 : -1;
    } else {
      position =
          parent.childrenStrategy.lookup(
              target, arcsIn, parent.positionBytes, parent.minChildrenLabel);
    }
//        System.out.println();
//        System.out.println(" lookup: " + target);
//        System.out.println(" parent info: ");
//        System.out.println(" parent label: " + parent.label);
//        System.out.println(" parent fp: " + parent.fp);
//        System.out.println(" parent min child label: " + parent.minChildrenLabel);
//        System.out.println(" parent strategy: " + parent.childrenStrategy);
//        System.out.println(" parent position bytes: " + parent.positionBytes);
//        arcsIn.seek(positionBytesFp);
//        System.out.println(" parent position bytes info: ");
//        IntStream.range(0, parent.positionBytes).forEach(i -> {
//          try {
//            System.out.print((arcsIn.readByte() & 0xFF) + ", ");
//          } catch (IOException e) {
//            throw new RuntimeException(e);
//          }
//        });
//        System.out.println();
//        System.out.println(" child position: " + position);
    if (position < 0) {
      return false;
    }
    arcsIn.seek(
        positionBytesFp + parent.positionBytes + (long) parent.childrenCodesBytes * position);
    //    System.out.println(" code fp: " + arcsIn.getFilePointer());
    long code = readNBytesLong(arcsIn, parent.childrenCodesBytes);
    child.label = target;
    load(child, code, parent);
    //    System.out.println(" child code: " + code);
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
