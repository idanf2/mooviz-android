package com.example.idanf.movies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.idanf.movies.R;
import com.example.idanf.movies.models.MovieDetails;
import com.example.idanf.movies.utils.DownloadImageTask;
import com.example.idanf.movies.utils.HttpRequest;
import com.example.idanf.movies.utils.Request;
import com.example.idanf.movies.utils.RequestTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    ArrayList<MovieDetails> moviesList;
    private String paramatarizedUrl;
    private int currentPage;
    Context movieContext;
    private final OnItemClickListener listener;


    public MoviesAdapter(Context context, ArrayList<MovieDetails> list, String paramatarizedUrl, int firstPage, OnItemClickListener listener) {
        movieContext = context;
        moviesList = list;

        this.paramatarizedUrl = paramatarizedUrl;
        this.currentPage = firstPage;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(MovieDetails item);
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == R.layout.movie_item) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_movies_item, parent, false);
        }

        return new MoviesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.ViewHolder holder, int position) {
        if (position == moviesList.size()) {
            holder.addItemsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPage++;
                    addMovies();
                }
            });
        } else {
            MovieDetails movie = moviesList.get(position);
            holder.title.setText(movie.title);
            if (movie.bitMap == null) {
                if (movie.imagePath != null && !movie.imagePath.equals("null")) {
                    new DownloadImageTask(holder.imageView, movie).execute("https://image.tmdb.org/t/p/w780//" + movie.imagePath);
                } else {
                    holder.imageView.setImageResource(R.mipmap.no_image_found);
                }
            } else {
                holder.imageView.setImageBitmap(movie.bitMap);
            }

            if (movie.rating == null || Double.parseDouble(movie.rating) != 0) {
                holder.rating.setText(movie.rating);
                holder.ratingContainer.setVisibility(View.VISIBLE);
            }

            holder.releaseDate.setText(movie.releaseDate);
            holder.bind(movie, listener);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void addMovies() {
        Object[] objArray = {currentPage};
        String url = new MessageFormat(paramatarizedUrl).format(objArray);
        new RequestTask() {
            @Override
            protected void onPostExecute(JSONObject res) {
                try {
                    JSONArray movies;
                    movies = res.getJSONArray("results");

                    for (int i = 0, len = movies.length(); i < len; i++) {
                        String title = movies.getJSONObject(i).getString("title");
                        String imagePath = movies.getJSONObject(i).getString("backdrop_path");
                        String vote_average = movies.getJSONObject(i).getString("vote_average");
                        String releaseDate = movies.getJSONObject(i).getString("release_date");
                        int id = movies.getJSONObject(i).getInt("id");

                        moviesList.add(new MovieDetails(title, imagePath, vote_average, releaseDate, id));
                    }
                    notifyItemInserted(moviesList.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.execute(new Request(url, HttpRequest.Method.GET, null));
    }

    @Override
    public int getItemCount() {
        return moviesList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == moviesList.size()) ? R.layout.add_movies_item : R.layout.movie_item;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public final TextView title;
        public final TextView rating;
        public final TextView releaseDate;
        public final RelativeLayout ratingContainer;

        public final ImageButton addItemsButton;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView1);
            title = view.findViewById(R.id.title);
            rating = view.findViewById(R.id.rating);
            ratingContainer = view.findViewById(R.id.ratingContainer);
            releaseDate = view.findViewById(R.id.releaseDate);
            addItemsButton = view.findViewById(R.id.addButton);
        }

        public void bind(final MovieDetails item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
