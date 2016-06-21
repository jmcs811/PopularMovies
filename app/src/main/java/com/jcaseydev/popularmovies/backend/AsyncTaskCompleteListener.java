package com.jcaseydev.popularmovies.backend;

import android.net.Uri;
import android.os.AsyncTask;

import com.jcaseydev.popularmovies.BuildConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by justinsMO on 4/26/2016.
 */
public class AsyncTaskCompleteListener extends AsyncTask<String, Void, List<Movie>>{


    private List<Movie> getDataFromJSon(String json)throws JSONException{

        //items that need to be got
        final String ARRAY_OF_MOVIES = "results";
        final String TITLE = "title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String VOTE_AVERAGE = "vote_average";
        final String MOVIE_ID = "id";

        //store the json in a json object
        JSONObject jsonObject = new JSONObject(json);

        //get the actual movie results
        JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_OF_MOVIES);

        //get the length of the array for looping
        int limit = jsonArray.length();

        //creat a list of movies
        List<Movie> movies = new ArrayList<>();

        //loop through all of the movies in the array
        for(int i = 0; i < limit; i++){
            //get the current movie object
            JSONObject movieObject = jsonArray.getJSONObject(i);

            //get the various details about the movie
            String title = movieObject.getString(TITLE);
            String poster = "http://image.tmdb.org/t/p/w154/" + movieObject.getString(POSTER_PATH);
            String overview = movieObject.getString(OVERVIEW);
            String releasedate = movieObject.getString(RELEASE_DATE);
            Double voteAvg = movieObject.getDouble(VOTE_AVERAGE);
            int movieId = movieObject.getInt(MOVIE_ID);

            //add the movie object to the movie list
            movies.add(new Movie(title, poster, overview, releasedate, voteAvg, movieId));
        }

        return movies;
    }



    @Override
    protected List<Movie> doInBackground(String... params) {

        try {

            final String QUERY_API = "api_key";

            //Build URL to get data from
            Uri builtUri = Uri.parse(params[0]).buildUpon()
                    .appendQueryParameter(QUERY_API, BuildConfig.TMDB_API_KEY)
                    .build();



            //Using OkHttp to make network request
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(builtUri.toString())
                    .build();

            //creating a response object
            Response response = null;

            try {
                //attempt to execute request
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Storing results of request in a string
            String jsonData = response.body().string();

            try {
                //attempt to call method that will extract data
                return getDataFromJSon(jsonData); //call to extract data
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
