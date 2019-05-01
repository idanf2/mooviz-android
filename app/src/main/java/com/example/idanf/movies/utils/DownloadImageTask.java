package com.example.idanf.movies.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.idanf.movies.models.IncludeImage;
import com.example.idanf.movies.models.MovieDetails;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    private IncludeImage includesImageObject;

    public DownloadImageTask(ImageView bmImage, IncludeImage includesImageObject) {
        this.bmImage = bmImage;
        this.includesImageObject = includesImageObject;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        this.includesImageObject.bitMap = result;
    }
}