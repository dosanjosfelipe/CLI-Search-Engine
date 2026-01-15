package me.search.ranking;

import me.search.core.Searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreCalculator {

    private final Searcher searcher = new Searcher();

    public Map<String, Double> rankingTFIDF(
            List<String> rootArgs,
            List<String> perfectArgs,
            Map<String, List<String>> rootTextHash,
            Map<String, List<String>> perfectTextHash,
            Map<String, List<Integer>> rootCounters,
            Map<String, List<Integer>> perfectCounters) {

        int totalDocs = rootTextHash.size();
        Map<String, Double> scores = new HashMap<>();

        // 1. Calcular DFs separados
        Map<String, Integer> rootDFs = searcher.getDocFrequencies(rootArgs, rootTextHash);
        Map<String, Integer> perfectDFs = searcher.getDocFrequencies(perfectArgs, perfectTextHash);

        for (String fileName : perfectTextHash.keySet()) {
            double tempScore = 0.0;
            List<Integer> pCounts = perfectCounters.get(fileName);
            List<Integer> rCounts = rootCounters.get(fileName);

            for (int i = 0; i < perfectArgs.size(); i++) {
                // TF e IDF para o termo exato
                int tfPerfect = pCounts.get(i);
                double idfPerfect = Math.log((double) totalDocs / (perfectDFs.get(perfectArgs.get(i)) + 1));

                // TF e IDF para a raiz
                int tfRoot = rCounts.get(i);
                double idfRoot = Math.log((double) totalDocs / (rootDFs.get(rootArgs.get(i)) + 1));

                // Soma ponderada com IDFs distintos
                tempScore += ((tfPerfect * idfPerfect * 1.0) + (tfRoot * idfRoot * 0.5));
            }
            scores.put(fileName, tempScore);
        }
        return scores;
    }
}
