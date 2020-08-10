package de.meyssam.saft;

public enum Language {

    DE("Deutsch"),
    EN("English");

    String namee;

    Language(String namee) {
        this.namee = namee;
    }

    public String getName() {
        return this.namee;
    }
}
