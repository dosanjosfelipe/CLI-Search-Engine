package me.search.core;

import me.search.text.PipelineFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Searcher {

    public Map<String, List<Integer>> argsCounter(List<String> args, Map<String, List<String>> fileHash) {
        Map<String, List<Integer>> countMap = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : fileHash.entrySet()) {
            List<Integer> countersList = new ArrayList<>();



            for (String arg : args) {
                long count = entry.getValue().stream().filter(token -> token.equals(arg)).count();
                countersList.add((int) count);

                if (entry.getKey().contains("Introdução APS")) {
                    System.out.println(args + "||||||||||" + arg);
                    System.out.println("DEBUG: Lendo arquivo: " + entry.getKey());
                    System.out.println("DEBUG: Total de termos no mapa deste arquivo: " + count);
                }
            }

            countMap.put(entry.getKey(), countersList);
        }

        return countMap;
    }

    public Map<String, Integer> getDocFrequencies(List<String> args, Map<String, List<String>> fileHash) {
        Map<String, Integer> dfMap = new HashMap<>();
        for (String arg : args) {
            int df = (int) fileHash.values().stream()
                    .filter(tokens -> tokens.contains(arg))
                    .count();
            dfMap.put(arg, df);
        }
        return dfMap;
    }
}
