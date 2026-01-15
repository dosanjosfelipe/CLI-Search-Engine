package me.search;

import me.search.cli.ArgumentParser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws IOException {
        ArgumentParser parser = new ArgumentParser();

        Logger.getLogger("org.apache.pdfbox").setLevel(Level.OFF);
        Logger.getLogger("org.apache.fontbox").setLevel(Level.OFF);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        parser.parse(args);
    }
}
