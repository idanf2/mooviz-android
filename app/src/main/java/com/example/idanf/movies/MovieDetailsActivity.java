package com.example.idanf.movies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.idanf.movies.adapters.CharacterAdapter;
import com.example.idanf.movies.models.Character;
import com.example.idanf.movies.models.MovieDetails;
import com.example.idanf.movies.utils.DownloadImageTask;
import com.example.idanf.movies.utils.HttpRequest;
import com.example.idanf.movies.utils.Request;
import com.example.idanf.movies.utils.RequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView title;
    private TextView overview;
    private TextView releaseDate;
    private TextView runtime;
    private TextView language;
    private TextView countries;
    private TextView genres;
    private TextView directors;
    private TextView producers;
    private TextView rating;
    private TextView voteCount;
    private TextView budget;
    private TextView releaseDateFirstDetails;
    private RelativeLayout ratingContainer;
    private RecyclerView charactersList;
    private ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details_activity);
        Toolbar toolbar = findViewById(R.id.movie_details_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.mipmap.movies_icon);

        title = findViewById(R.id.detailsTitle);
        overview = findViewById(R.id.overview);
        overview.setMovementMethod(new ScrollingMovementMethod());
        releaseDate = findViewById(R.id.releaseDate);
        poster = findViewById(R.id.moviePoster);
        runtime = findViewById(R.id.runtime);
        language = findViewById(R.id.languages);
        countries = findViewById(R.id.countries);
        genres = findViewById(R.id.genres);
        directors = findViewById(R.id.directors);
        producers = findViewById(R.id.producers);
        rating = findViewById(R.id.ratingDetails);
        voteCount = findViewById(R.id.voteCount);
        budget = findViewById(R.id.budget);
        releaseDateFirstDetails = findViewById(R.id.releaseDateFirstDetails);
        ratingContainer = findViewById(R.id.ratingDetailsContainer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        charactersList = findViewById(R.id.characters);
        charactersList.setLayoutManager(layoutManager);
        getMovieDetailsAndRenderUI();

    }

    @SuppressLint("StaticFieldLeak")
    private void getMovieDetailsAndRenderUI() {
        Bundle b = getIntent().getExtras();
        int movieId = -1;
        if (b != null) {
            movieId = b.getInt("movieId");
        }

        final int finalMovieId = movieId;

        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=3a246368687fa6f8ad378b0bf1c8c630&language=en-US";
        new RequestTask() {
            @Override
            protected void onPostExecute(JSONObject movie) {
                try {
                    String movieTitle = movie.getString("title");
                    String vote_average = movie.getString("vote_average");
                    String vote_count = movie.getString("vote_count");
                    String budgetString = movie.getString("budget");
                    String movieReleaseDate = movie.getString("release_date");
                    String movieOverview = movie.getString("overview");
                    String posterPath = movie.getString("poster_path");
                    JSONArray movieGenres = movie.getJSONArray("genres");
                    JSONArray movieCountries = movie.getJSONArray("production_countries");
                    JSONArray movieLanguage = movie.getJSONArray("spoken_languages");
                    int movieRuntime = movie.getInt("runtime");

                    final ArrayList<String> genresString = new ArrayList<>();
                    for (int i = 0, len = movieGenres.length(); i < len; i++) {
                        genresString.add(movieGenres.getJSONObject(i).getString("name"));
                    }

                    final ArrayList<String> countriesString = new ArrayList<>();
                    for (int i = 0, len = movieCountries.length(); i < len; i++) {
                        if (movieCountries.getJSONObject(i).getString("iso_3166_1").equals("US")) {
                            countriesString.add("USA");
                        } else {
                            countriesString.add(movieCountries.getJSONObject(i).getString("name"));
                        }
                    }

                    final ArrayList<String> languagesString = new ArrayList<>();
                    for (int i = 0, len = movieLanguage.length(); i < len; i++) {
                        languagesString.add(movieLanguage.getJSONObject(i).getString("name"));
                    }

                    MovieDetails movieDetails = new MovieDetails(movieTitle, posterPath, vote_average, movieReleaseDate, finalMovieId);

                    title.setText(movieTitle);
                    if (vote_average == null || Double.parseDouble(vote_average) != 0) {
                        rating.setText(vote_average);
                        voteCount.setText(vote_count);
                        ratingContainer.setVisibility(View.VISIBLE);
                    }
                    overview.setText(movieOverview);
                    releaseDate.setText(movieReleaseDate);
                    runtime.setText(movieRuntime + "");
                    genres.setText(TextUtils.join("\n", genresString));
                    releaseDateFirstDetails.setText(movieReleaseDate.split("-")[0]);
                    countries.setText(TextUtils.join("\n", countriesString));
                    language.setText(TextUtils.join("\n", languagesString));
                    budget.setText(Integer.parseInt(budgetString) / 1000000 + "M $");
                    new DownloadImageTask(poster, movieDetails).execute("https://image.tmdb.org/t/p/w780//" + movieDetails.imagePath);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute(new Request(url, HttpRequest.Method.GET, null));


        String castUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=3a246368687fa6f8ad378b0bf1c8c630";

        new RequestTask() {
            @Override
            protected void onPostExecute(JSONObject movie) {
                try {
                    JSONArray movieCrew = movie.getJSONArray("crew");
                    final ArrayList<String> directorsList = new ArrayList<>();
                    final ArrayList<String> producersList = new ArrayList<>();

                    for (int i = 0, len = movieCrew.length(); i < len; i++) {
                        if (movieCrew.getJSONObject(i).getString("job").equals("Director")) {
                            directorsList.add(movieCrew.getJSONObject(i).getString("name"));
                        } else if (movieCrew.getJSONObject(i).getString("job").equals("Executive Producer")) {
                            producersList.add(movieCrew.getJSONObject(i).getString("name"));
                        }
                    }

                    directors.setText(TextUtils.join("\n", directorsList));
                    producers.setText(TextUtils.join("\n", producersList));


                    JSONArray movieCast = movie.getJSONArray("cast");

                    ArrayList<Character> characters = new ArrayList<>();
                    for (int i = 0, len = movieCast.length(); i < len; i++) {
                        characters.add(new Character(movieCast.getJSONObject(i).getString("name"), movieCast.getJSONObject(i).getString("profile_path")));
                    }

                    charactersList.setAdapter(new CharacterAdapter(characters));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute(new Request(castUrl, HttpRequest.Method.GET, null));
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
