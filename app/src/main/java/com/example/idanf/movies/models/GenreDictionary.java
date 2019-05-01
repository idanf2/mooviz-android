package com.example.idanf.movies.models;

import com.example.idanf.movies.R;

import java.util.HashMap;
import java.util.Map;

public class GenreDictionary {

    private static Map<Integer, String> genreMap = new HashMap<Integer, String>();

    public GenreDictionary() {
        genreMap.put(R.id.genere_all, null);
        genreMap.put(R.id.genere_action, "28");
        genreMap.put(R.id.genere_adventure, "12");
        genreMap.put(R.id.genere_animation, "16");
        genreMap.put(R.id.genere_comedy, "35");
        genreMap.put(R.id.genere_crime, "80");
        genreMap.put(R.id.genere_documentary, "99");
        genreMap.put(R.id.genere_drama, "18");
        genreMap.put(R.id.genere_family, "10751");
        genreMap.put(R.id.genere_fantasy, "14");
        genreMap.put(R.id.genere_history, "36");
        genreMap.put(R.id.genere_horror, "27");
        genreMap.put(R.id.genere_music, "10402");
        genreMap.put(R.id.genere_mystery, "9648");
        genreMap.put(R.id.genere_romance, "10749");
        genreMap.put(R.id.genere_science_fiction, "878");
        genreMap.put(R.id.genere_tv_movie, "10770");
        genreMap.put(R.id.genere_thriller, "53");
        genreMap.put(R.id.genere_war, "10752");
        genreMap.put(R.id.genere_western, "37");
    }

    public String getGenreId(int menuItemId) {
        return genreMap.get(menuItemId);
    }
}
