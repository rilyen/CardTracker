package ca.cmpt213.webserver.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TokimonList {

    private static final String FILE_PATH = "src/main/resources/static/tokimons.json";
    private List<Tokimon> tokimons = new ArrayList<>();
    private Gson gson = new Gson();

    public TokimonList() {
        ensureFileExists(); // Ensure the file exists
        loadFromFile();
    }

    public List<Tokimon> getTokimons() {
        return tokimons;
    }

    public Tokimon getTokimonById(long id) {
        for (Tokimon tokimon : tokimons) {
            if (tokimon.getId() == id) {
                return tokimon;
            }
        }
        return null;
    }

    public void addTokimon(Tokimon newTokimon) {
        tokimons.add(newTokimon);
        saveToFile();
    }

    public Tokimon deleteTokimon(long id) {
        for (int i = 0; i < tokimons.size(); i++) {
            if (tokimons.get(i).getId() == id) {
                Tokimon tokimon = tokimons.remove(i);
                saveToFile();
                return tokimon;
            }
        }
        return null;
    }

    public Tokimon updateTokimon(Tokimon updatedTokimon) {
        long id = updatedTokimon.getId();
        Tokimon tokimon = getTokimonById(id);
        if (tokimon != null) {
            String updatedName = updatedTokimon.getName();
            String updatedType = updatedTokimon.getType();
            int updatedRarityScore = updatedTokimon.getRarityScore();

            if (!"null".equals(updatedName)) {
                tokimon.setName(updatedName);
            }
            if (!"null".equals(updatedType)) {
                tokimon.setType(updatedType);
            }
            if (updatedRarityScore != -1) {
                tokimon.setRarityScore(updatedRarityScore);
            }
            saveToFile();
        }
        return getTokimonById(id);
    }

    private void ensureFileExists() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file.");
                e.printStackTrace();
            }
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists() && file.length() > 0) {
            try (FileReader reader = new FileReader(file)) {
                Type tokimonListType = new TypeToken<List<Tokimon>>() {}.getType();
                tokimons = gson.fromJson(reader, tokimonListType);
                if (tokimons == null) {
                    tokimons = new ArrayList<>();
                }
            } catch (IOException e) {
                System.out.println("IO Exception while loading: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            tokimons = new ArrayList<>();
        }
    }

    private void saveToFile() {
        File file = new File(FILE_PATH);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(tokimons, writer);
        } catch (IOException e) {
            System.out.println("IO Exception while saving: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
