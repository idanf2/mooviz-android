package com.example.idanf.movies.models;

public class Character extends IncludeImage{
    public String name;
    public String imagePath;

    public Character(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }
}
