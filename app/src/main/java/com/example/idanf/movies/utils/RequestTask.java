package com.example.idanf.movies.utils;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

public class RequestTask extends AsyncTask<Request, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(Request... req) {
        if(req != null && req.length > 0){
            try{
                Request r = req[0];
                HttpRequest http = new HttpRequest(r.url).prepare(r.method);
                if(r.method == HttpRequest.Method.POST || r.method == HttpRequest.Method.PUT){
                    http.withData(r.data.toString());
                }
                return http.sendAndReadJSON();
            }catch (JSONException | IOException e){
                //Failed...
                e.printStackTrace();
            }
        }
        return null;
    }
}
