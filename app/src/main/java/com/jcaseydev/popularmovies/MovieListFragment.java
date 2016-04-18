package com.jcaseydev.popularmovies;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
 * Created by justinsMO on 2/24/2016.
 */
public class MovieListFragment extends Fragment {

    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private ArrayList<String> listOfMoviePosters = new ArrayList<>();
    private String MOVIE_KEY = "movie_info";
    private ImageAdapter imageAdapter;
    private String BASE_URL = "http://api.themoviedb.org/3/movie/now_playing";
    int toggle = 0;

    public MovieListFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        updateMovies();

    }

    private void updateMovies() {
        movieArrayList.clear();
        FetchMovieData fmd = new FetchMovieData();
        fmd.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.movie_list_fragment, container, false);
        imageAdapter = new ImageAdapter(getActivity(), listOfMoviePosters);

        GridView mGridView = (GridView) rootview.findViewById(R.id.movie_list_grid_view);
        mGridView.setAdapter(imageAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie details = movieArrayList.get(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(MOVIE_KEY, details);
                startActivity(intent);
            }
        });



        return rootview;
    }

    public class FetchMovieData extends AsyncTask<Void, Void, List<Movie>> {

        private List<Movie> getDataFromJSon(String json)throws JSONException{

            //items that need to be got
            final String ARRAY_OF_MOVIES = "results";
            final String TITLE = "title";
            final String POSTER_PATH = "poster_path";
            final String OVERVIEW = "overview";
            final String RELEASEDATE = "release_date";
            final String VOTE_AVERAGE = "vote_average";
            final String MOVIE_ID = "id";

            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(ARRAY_OF_MOVIES);

            int limit = jsonArray.length();
            List<Movie> movies = new ArrayList<>();

            for(int i = 0; i < limit; i++){
                JSONObject movieObject = jsonArray.getJSONObject(i);

                String title = movieObject.getString(TITLE);
                String poster = "http://image.tmdb.org/t/p/w342/" + movieObject.getString(POSTER_PATH);
                String overview = movieObject.getString(OVERVIEW);
                String releasedate = movieObject.getString(RELEASEDATE);
                Double voteAvg = movieObject.getDouble(VOTE_AVERAGE);
                int movieId = movieObject.getInt(MOVIE_ID);

                movies.add(new Movie(title, poster, overview, releasedate, voteAvg, movieId));
            }

            return movies;
        }


        @Override
        protected List<Movie> doInBackground(Void... params) {

            try {

                final String QUERY_API = "api_key";

                //Build URL to get data from
                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
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

        @Override
        protected void onPostExecute(List<Movie> result) {
            if (result != null){
                //add all of the movie objects that have be gathered
                //into the movieArrayList
                movieArrayList.addAll(result);
                //Call updatePoster to get movie posters
                updatePoster();

                //notify the image adapter that there is data now
                imageAdapter.notifyDataSetChanged();

            }
        }
        public void updatePoster(){
            //for every movie in movieArraylist add to
            //listOfMoviePosters
            for (Movie movie : movieArrayList){
                listOfMoviePosters.add(movie.getMoviePoster());
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_list_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(id == R.id.sort_action) {
            if (toggle == 0) {
                BASE_URL = "http://api.themoviedb.org/3/movie/top_rated";
                movieArrayList.clear();
                listOfMoviePosters.clear();
                updateMovies();
                toggle = 1;
            } else if(toggle == 1){
                BASE_URL = "http://api.themoviedb.org/3/movie/now_playing";
                movieArrayList.clear();
                listOfMoviePosters.clear();
                updateMovies();
                toggle = 0;
            }
        }
            return super.onOptionsItemSelected(item);

    }
}
