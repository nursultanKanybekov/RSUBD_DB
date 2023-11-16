package org.example.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ReadFromFile {
    public List<String> readFile() {
        try {
            // Read all lines from the file and store them in a List<String>
            List<String> lines = Files.readAllLines(Paths.get("checker.txt"), StandardCharsets.UTF_8);
            return lines;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
