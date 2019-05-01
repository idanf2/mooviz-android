package com.example.idanf.movies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.idanf.movies.R;
import com.example.idanf.movies.models.MovieDetails;
import com.example.idanf.movies.utils.DownloadImageTask;

public class OldMoviesAdapter extends BaseAdapter {

    private final Context context;
    private final MovieDetails[] movies;

    public OldMoviesAdapter(Context context, MovieDetails[] movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.length;//how many data objects
    }

    @Override
    public MovieDetails getItem(int i) {
        return movies[i];//raw object by index
    }

    @Override
    public long getItemId(int i) {
        return i;//id of an object
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if(convertView == null){//if not created yet
            //create the view by given layout resource
            convertView = LayoutInflater.from(context).inflate(R.layout.movie_item, null);
        }
        MovieDetails movie = getItem(i);//get relevant Country by index
        ((TextView)convertView.findViewById(R.id.title)).setText(movie.title);
        if(movie.bitMap == null) {
            new DownloadImageTask((ImageView) convertView.findViewById(R.id.imageView1), movie).execute("https://image.tmdb.org/t/p/w92//" + movie.imagePath);
        } else {
            ((ImageView) convertView.findViewById(R.id.imageView1)).setImageBitmap(movie.bitMap);
        }
        return convertView;//return converted view
    }

}
