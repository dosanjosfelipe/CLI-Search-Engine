package me.search.cli;

import me.search.indexing.FileScanner;
import me.search.core.Searcher;
import me.search.ranking.ScoreCalculator;
import me.search.text.PipelineFormatter;
import me.search.utils.NameFormatter;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class ArgumentParser {
    PipelineFormatter pipelineFormatter = new PipelineFormatter();
    ScoreCalculator scoreCalculator = new ScoreCalculator();
    FileScanner fileScanner = new FileScanner();
    Searcher searcher = new Searcher();
    NameFormatter nameFormatter = new NameFormatter();

    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_ORANGE = "\u001B[38;5;208m";
    static final String ANSI_YELLOW = "\u001B[33m";
    static final String ANSI_GREEN = "\u001B[32m";

    public void parse(String[] args) throws IOException {
        List<String> rootArgs = pipelineFormatter.apply(List.of(args), true);
        List<String> perfectArgs = pipelineFormatter.apply(List.of(args), false);

        if (rootArgs.isEmpty()) {
            System.out.print("Use: search <args> with valid terms");
            return;
        }
        System.out.print(" - Wait a minute...");
        System.out.flush();

        Map<String, List<String>> fileHash = fileScanner.readFilesBase();
        Map<String, List<String>> rootTextHash = new HashMap<>();
        Map<String, List<String>> perfectTextHash = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : fileHash.entrySet()) {
            List<String> formattedStemText = pipelineFormatter.apply(entry.getValue(), true);
            List<String> formattedText = pipelineFormatter.apply(entry.getValue(), false);

            rootTextHash.put(entry.getKey(), formattedStemText);
            perfectTextHash.put(entry.getKey(), formattedText);
        }

        Map<String, List<Integer>> rootCounter = searcher.countTermFrequency(rootArgs, rootTextHash);
        Map<String, List<Integer>> perfectCounter = searcher.countTermFrequency(perfectArgs, perfectTextHash);

// -------------------- TERMINAL ---------------------
        if (!fileHash.isEmpty()) {
            Map<String, Double> score = scoreCalculator.defineScore(rootArgs, perfectArgs,
                    rootTextHash, perfectTextHash, rootCounter, perfectCounter);

            int LIMIT_NAME = 40;

            System.out.print("\r" + " ".repeat(20) + "\r");

            System.out.println("\n----------------------- RESULTS -----------------------");
            System.out.printf("%-40s | %s%n", "File Name", "Relevance");
            System.out.println("-------------------------------------------------------");

            score.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .forEach(entry -> {
                        String fileName = Paths.get(entry.getKey()).getFileName().toString();
                        String name = nameFormatter.formatFileName(fileName, LIMIT_NAME);

                        double grade = entry.getValue();

                        String color;
                        if (grade == 0) {
                            color = ANSI_RED;
                        } else if (grade <= 299) {
                            color = ANSI_ORANGE;
                        } else if (grade <= 599) {
                            color = ANSI_YELLOW;
                        } else {
                            color = ANSI_GREEN;
                        }

                        System.out.printf("%-40s | %s%7.2f pts%s%n", name, color, grade, ANSI_RESET);
                    });
            System.out.println("-------------------------------------------------------");
            System.out.println(" ");
        } else {
            System.out.println(" ");
            System.out.println("There are no files to read here.");
            System.out.println(" ");
        }
    }
}
