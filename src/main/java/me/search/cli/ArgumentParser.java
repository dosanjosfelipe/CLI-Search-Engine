package me.search.cli;

import me.search.indexing.FileScanner;
import me.search.core.Searcher;
import me.search.ranking.ScoreCalculator;
import me.search.text.PipelineFormatter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ArgumentParser {
    PipelineFormatter pipelineFormatter = new PipelineFormatter();
    public void parse(String[] args) throws IOException {
        PipelineFormatter formatterApplier = new PipelineFormatter();
        ScoreCalculator scoreCalculator = new ScoreCalculator();
        FileScanner fileScanner = new FileScanner();
        Searcher searcher = new Searcher();

        // -------------------- ARGS ---------------------
        if (args.length == 0) {
            System.out.print("Use: search <args>");
            return;
        }

        List<String> rootArgs = formatterApplier.apply(List.of(args), true);
        List<String> perfectArgs = formatterApplier.apply(List.of(args), false);

        if (rootArgs.isEmpty()) {
            System.out.print("Use: search <args> with valid terms");
            return;
        }

        StringBuilder query = new StringBuilder();

        for (String arg : args) {
            query.append(arg).append(" ");
        }

        String searchTerms = query.toString().trim();

        Map<String, List<String>> fileHash = fileScanner.readFilesBase();

        Map<String, List<String>> rootTextHash = new HashMap<>();
        Map<String, List<String>> perfectTextHash = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : fileHash.entrySet()) {
            List<String> formattedStemText = pipelineFormatter.apply(entry.getValue(), true);
            List<String> formattedText = pipelineFormatter.apply(entry.getValue(), false);

            rootTextHash.put(entry.getKey(), formattedStemText);
            perfectTextHash.put(entry.getKey(), formattedText);
        }


        Map<String, List<Integer>> rootCounter = searcher.argsCounter(rootArgs, rootTextHash);
        Map<String, List<Integer>> perfectCounter = searcher.argsCounter(perfectArgs, perfectTextHash);

        // -------------------- TERMINAL ---------------------
        if (!fileHash.isEmpty()) {
            Map<String, Double> score = scoreCalculator.rankingTFIDF(rootArgs, perfectArgs,
                    rootTextHash, perfectTextHash, rootCounter, perfectCounter);

            double maxRawScore = Collections.max(score.values());
            int LIMIT_NAME = 40;

            System.out.println("\n----------------------- RESULTS -----------------------");
            System.out.printf("%-40s | %s%n", "File Name", "Relevance");
            System.out.println("-------------------------------------------------------");

            score.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .forEach(entry -> {
                        String fullPath = entry.getKey();
                        String fileName = Paths.get(fullPath).getFileName().toString();

                        // 1. Limita o nome do arquivo
                        String nomeExibicao = formatFileName(fileName, LIMIT_NAME);

                        // 2. Calcula a nota (0 a 100)
                        double nota = (maxRawScore > 0) ? (entry.getValue() / maxRawScore) * 100 : 0;


                        System.out.printf("%-40s | %6.2f%%%n", nomeExibicao, nota);

                    });
            System.out.println("-------------------------------------------------------");
        } else {
            System.out.println(" ");
            System.out.println("There are no files to read here.");
            System.out.println(" ");
        }
    }
    public String formatFileName(String name, int limit) {
        if (name.length() <= limit) {
            return name;
        }
        // Corta o nome e adiciona reticências
        return name.substring(0, limit - 3) + "...";
    }
}
