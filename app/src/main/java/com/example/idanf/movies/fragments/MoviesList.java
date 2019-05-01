package com.example.idanf.movies.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idanf.movies.MovieDetailsActivity;
import com.example.idanf.movies.R;
import com.example.idanf.movies.adapters.MoviesAdapter;
import com.example.idanf.movies.models.MovieDetails;
import com.example.idanf.movies.utils.HttpRequest;
import com.example.idanf.movies.utils.Request;
import com.example.idanf.movies.utils.RequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by coderzpassion on 05/04/16.
 */
public class MoviesList extends Fragment {

    private String url;
    private String paramatarizedUrl;
    private MoviesAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.movies_list, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int firstPage = 1;

            paramatarizedUrl = bundle.getString("url");
            Object[] objArray = {firstPage};
            url = new MessageFormat(paramatarizedUrl).format(objArray);
        }

        setUpRecyclerView(recyclerView);


        return recyclerView;
    }

    private void setUpRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        if (url != null) {
            loadApiData(rv);
        }
    }

    public void updateMovies() {
        if (recyclerView != null) {
            Bundle bundle = this.getArguments();
            if (bundle != null) {
                int firstPage = 1;

                paramatarizedUrl = bundle.getString("url");
                Object[] objArray = {firstPage};
                url = new MessageFormat(paramatarizedUrl).format(objArray);
            }
            loadApiData(recyclerView);
        }
    }


    @SuppressLint("StaticFieldLeak")
    private void loadApiData(final RecyclerView rv) {
        AsyncTask<Request, Void, JSONObject> asyncTask = new RequestTask() {
            @Override
            protected void onPostExecute(JSONObject res) {
                try {
                    JSONArray movies;
                    movies = res.getJSONArray("results");

                    final ArrayList<MovieDetails> itemsMovies = new ArrayList<>();
                    for (int i = 0, len = movies.length(); i < len; i++) {
                        String title = movies.getJSONObject(i).getString("title");
                        String imagePath = movies.getJSONObject(i).getString("backdrop_path");
                        String vote_average = movies.getJSONObject(i).getString("vote_average");
                        String releaseDate = movies.getJSONObject(i).getString("release_date");
                        int id = movies.getJSONObject(i).getInt("id");

                        itemsMovies.add(new MovieDetails(title, imagePath, vote_average, releaseDate, id));
                    }

                    adapter = new MoviesAdapter(rv.getContext(), itemsMovies, paramatarizedUrl, 1, new MoviesAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(MovieDetails item) {
                            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                            Bundle b = new Bundle();
                            b.putInt("movieId", item.id);
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });
                    rv.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Request(url, HttpRequest.Method.GET, null));
    }
}