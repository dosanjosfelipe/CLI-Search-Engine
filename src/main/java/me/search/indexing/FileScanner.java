package me.search.indexing;

import java.io.*;
import java.util.*;

public class FileScanner {

    public List<String> listFiles() {
        File folder = new File(System.getProperty("user.dir"));
        List<String> filesPath = new ArrayList<>();
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    filesPath.add(file.getAbsolutePath());
                }
            }
        } else {
            return filesPath;
        }
        return filesPath;
    }

    public Map<String, List<String>> readFilesBase() {
        List<String> filesPath = listFiles();
        Map<String, List<String>> filesHash = new HashMap<>();
        Readers readers = new Readers();

        try {
            for (String path : filesPath) {
                File file = new File(path);

                if (FileVerification.isPdfFile(file)) {
                    filesHash.put(path, readers.pdfReader(file));

                } else if (FileVerification.isJsonFile(file)) {
                    filesHash.put(path, readers.jsonReader(file));

                } else if (FileVerification.isDocxFile(file)) {
                    filesHash.put(path, readers.docxReader(file));

                } else if (FileVerification.isHtmlFile(file)) {
                    filesHash.put(path, readers.htmlReader(file));

                } else if (FileVerification.isTextFile(file)){
                    filesHash.put(path, readers.txtReader(file));

                }
            }
        } catch (IOException e) {
            System.out.println("Fail to read the file: " + e);
        }
        return filesHash;
    }
}
