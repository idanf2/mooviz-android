package com.example.idanf.movies;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.idanf.movies.fragments.MoviesList;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.mipmap.movies_icon);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setUpFragment(query);
        }
    }

    private void setUpFragment(String query) {
        Fragment movies = new MoviesList();
        Bundle bundle = new Bundle();
        String queryUri = query.replaceAll(" ", "+");
        bundle.putString("url", "http://api.themoviedb.org/3/search/movie/?api_key=3a246368687fa6f8ad378b0bf1c8c630&language=en-US&query=" + queryUri + "&page={0}");
        movies.setArguments(bundle);

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, movies);//replace to it
        transaction.addToBackStack(null);//no name to attach
        transaction.commit();//execute transaction

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
