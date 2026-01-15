package me.search.text;


import java.util.List;

public class PipelineFormatter {

    public List<String> apply(List<String> text, boolean stem) {
        Formatter formatter = new Formatter();

        List<String> normalizedText = formatter.normalizer(text);
        List<String> noStopWordsText = formatter.stopWords(normalizedText);

        if (stem) {
            return formatter.stem(noStopWordsText);
        } else {
            return noStopWordsText;
        }

    }
}
