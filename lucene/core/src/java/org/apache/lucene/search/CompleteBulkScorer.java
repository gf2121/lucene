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
package org.apache.lucene.search;

import java.io.IOException;
import org.apache.lucene.util.Bits;

/**
 * A bulk scorer used when score mode is {@link ScoreMode#COMPLETE} and {@link
 * Scorer#nextDocsAndScores} has optimizations to run faster than one-by-one iteration.
 */
class CompleteBulkScorer extends BulkScorer {

  private static final int SPARSE_THRESHOLD = 16;
  private final SimpleScorable scorable = new SimpleScorable();
  private final DocAndScoreBuffer buffer = new DocAndScoreBuffer();
  private final Scorer scorer;

  CompleteBulkScorer(Scorer scorer) {
    this.scorer = scorer;
  }

  @Override
  public int score(LeafCollector collector, Bits acceptDocs, int min, int max) throws IOException {
    collector.setScorer(scorable);
    if (scorer.docID() < min) {
      scorer.iterator().advance(min);
    }
    for (scorer.nextDocsAndScores(max, acceptDocs, buffer);
        buffer.size > 0;
        scorer.nextDocsAndScores(max, acceptDocs, buffer)) {
      int collected = 0;
      for (int i = 0, size = buffer.size; i < size; i++) {
        float score = scorable.score = buffer.scores[i];
        if (score >= scorable.minCompetitiveScore) {
          collected ++;
          collector.collect(buffer.docs[i]);
        }
      }
      scorer.setMinCompetitiveScore(scorable.minCompetitiveScore);
      if (collected < SPARSE_THRESHOLD) {
        collectSparseCompetitive(collector, acceptDocs, max);
        break;
      }
    }
    return scorer.docID();
  }

  private void collectSparseCompetitive(LeafCollector collector, Bits acceptDocs, int max) throws IOException {
    DocIdSetIterator iterator = scorer.iterator();
    for (int doc = iterator.docID(); doc < max; doc = iterator.nextDoc()) {
      if (acceptDocs == null || acceptDocs.get(doc)) {
        collector.collect(doc);
        scorer.setMinCompetitiveScore(scorable.minCompetitiveScore);
      }
    }
  }

  @Override
  public long cost() {
    return scorer.iterator().cost();
  }
}
