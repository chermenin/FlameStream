package com.spbsu.datastream.core.wordcount;

import java.util.List;
import java.util.function.Function;

/**
 * User: Artem
 * Date: 25.06.2017
 */
public class CountWordEntries implements Function<List<WordContainer>, WordCounter> {

  @Override
  public WordCounter apply(List<WordContainer> wordContainers) {
    if (wordContainers.size() == 1) {
      final WordEntry wordEntry = (WordEntry) wordContainers.get(0);
      return new WordCounter(wordEntry.word(), 1);
    } else {
      final WordCounter counter = (WordCounter) wordContainers.get(0);
      return new WordCounter(counter.word(), counter.count() + 1);
    }
  }
}
