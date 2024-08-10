package ca.cmpt213.webserver.models;

public class Tokimon {
    private String name;
    private String type;
    private int rarityScore;
    private long id;

    public Tokimon(String name, String type, int rarityScore) {
        this.name = name;
        this.type = type;
        this.rarityScore = rarityScore;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setRarityScore(int rarityScore) {
        this.rarityScore = rarityScore;
    }

    public int getRarityScore() {
        return rarityScore;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
