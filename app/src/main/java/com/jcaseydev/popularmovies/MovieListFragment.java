package com.jcaseydev.popularmovies;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        //clear out both lists
        movieArrayList.clear();
        listOfMoviePosters.clear();

        //create a new async task and execute
        FetchMovieData fmd = new FetchMovieData();
        fmd.execute(BASE_URL);
        Toast.makeText(getContext(), "UpdateMoviesCalled", Toast.LENGTH_SHORT).show();
    }

    public class FetchMovieData extends AsyncTaskCompleteListener {

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

        //simple way to toggle the sort order
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
