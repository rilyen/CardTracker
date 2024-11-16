package ca.cmpt213.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;

public class ProcessInput {

    public static Tokimon readInputToCard(String content) {
        Gson gson = new Gson();
        Tokimon tokimon = null;
        try {
            tokimon = gson.fromJson(content, Tokimon.class);
            System.out.println("Name: " + tokimon.getName());
            System.out.println("Type: " + tokimon.getType());
            System.out.println("Rarity score: " + tokimon.getRarityScore());
            System.out.println("Id: " + tokimon.getId());
        } catch (JsonSyntaxException e) {
            System.out.println("ERROR: " + e.getMessage());
            System.out.println(content + " has bad Json file format.");
        }
        return tokimon;
    }

}
