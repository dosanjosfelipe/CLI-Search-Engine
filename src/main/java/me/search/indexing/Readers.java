package me.search.indexing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.List;
import java.util.Map;

public class Readers {
    public List<String> pdfReader(File file) {
        try (PDDocument pdf = PDDocument.load(file)) {

            PDFTextStripper stripper = new PDFTextStripper();

            return List.of(stripper.getText(pdf).split("\\s+"));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> txtReader(File file) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());

            }
            return List.of(sb.toString().split("\\s+"));
        }
    }

    public List<String> htmlReader(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");

        doc.select("script, style, noscript").remove();

        return List.of(doc.text().split("\\s+"));
    }

    public List<String> jsonReader(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> json = mapper.readValue(file, new TypeReference<Map<String, Object>>() {});

        return List.of(mapper.writeValueAsString(json).split("\\s+"));
    }

    public List<String> docxReader(File file) throws IOException {
        StringBuilder text = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument doc = new XWPFDocument(fis)) {

            for (XWPFParagraph p : doc.getParagraphs()) {
                text.append(p.getText()).append("\n");
            }
        }
        return List.of(text.toString().split("\\s+"));
    }
}

