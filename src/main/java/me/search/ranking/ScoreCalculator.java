package me.search.ranking;

import me.search.core.Searcher;

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

        // DF calculado apenas sobre os termos raiz (correto)
        Map<String, Integer> rootDFs =
                searcher.getDocFrequencies(rootArgs, rootTextHash);

        // Comprimento médio real dos documentos
        double avgLength = rootTextHash.values().stream()
                .mapToInt(List::size)
                .average()
                .orElse(1.0);

        final double k = 1.5;
        final double b = 0.75;

        for (String fileName : perfectTextHash.keySet()) {

            double score = 0.0;

            List<Integer> perfectCounts = perfectCounters.get(fileName);
            List<Integer> rootCounts = rootCounters.get(fileName);

            int docLength = rootTextHash.get(fileName).size();

            double lengthNorm = 1.0 - b + b * (docLength / avgLength);

            for (int i = 0; i < perfectArgs.size(); i++) {

                int tfPerfect = perfectCounts.get(i);
                int tfRoot = rootCounts.get(i);

                if (tfPerfect == 0 && tfRoot == 0) {
                    continue;
                }

                double tfPerfectBM25 =
                        (tfPerfect * (k + 1.0)) /
                                (tfPerfect + k * lengthNorm);

                double tfRootBM25 =
                        (tfRoot * (k + 1.0)) /
                                (tfRoot + k * lengthNorm);

                double tfCombined =
                        (2.0 * tfPerfectBM25) +
                                (tfRootBM25);

                int df = rootDFs.getOrDefault(rootArgs.get(i), 0);
                double idf = Math.log(1.0 + (double) totalDocs / (1.0 + df));

                score += (tfCombined * idf) * 60;
            }

            score /= perfectArgs.size();

            scores.put(fileName, score);
        }

        return scores;
    }
}
