package com.example.idanf.movies.models;

public class MovieDetails extends IncludeImage {
    public int id;
    public String title;
    public String imagePath;
    public String rating;
    public String releaseDate;

    public MovieDetails(String title, String imagePath, String rating, String releaseDate, int id) {
        this.title = title;
        this.imagePath = imagePath;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.id = id;
    }

}
