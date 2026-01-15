package me.search.utils;

public class NameFormatter {
    public String formatFileName(String name, int limit) {
        if (name.length() <= limit) {
            return name;
        }
        return name.substring(0, limit - 3) + "...";
    }
}
