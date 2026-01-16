package me.search.core;

import java.util.*;

public class Searcher {

    public Map<String, List<Integer>> countTermFrequency(List<String> args, Map<String, List<String>> fileHash) {
        Map<String, List<Integer>> countMap = new HashMap<>();

        for (var entry : fileHash.entrySet()) {
            Map<String, Integer> freq = new HashMap<>();

            for (String token : entry.getValue()) {
                freq.merge(token, 1, Integer::sum);
            }

            List<Integer> docCounts = new ArrayList<>();
            for (String arg : args) {
                docCounts.add(freq.getOrDefault(arg, 0));
            }
            countMap.put(entry.getKey(), docCounts);
        }
        return countMap;
    }

    public Map<String, Integer> getDocFrequencies(List<String> args, Map<String, List<String>> fileHash) {
        Map<String, Integer> dfMap = new HashMap<>();

        for (String arg : args) {
            dfMap.put(arg, 0);
        }

        for (var entry : fileHash.entrySet()) {

            Set<String> uniqueTokens =
                    new HashSet<>(entry.getValue());

            for (String token : uniqueTokens) {
                if (dfMap.containsKey(token)) {
                    dfMap.merge(token, 1, Integer::sum);
                }
            }
        }
        return dfMap;
    }
}
