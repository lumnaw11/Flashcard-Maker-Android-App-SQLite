package jp.ac.jec.cm0145.flashcardmaker;


public class Card {
    private String definition;
    private String word;
    private int id;

    public Card(String definition, String word, int id) {
        this.definition = definition;
        this.word = word;
        this.id = id;
    }

    public Card(String definition, String word) {
        this.definition = definition;
        this.word = word;
    }

    public String getdefinition() {
        return definition;
    }

    public void setdefinition(String definition) {
        this.definition = definition;
    }

    public String getword() {
        return word;
    }

    public void setword(String word) {
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
