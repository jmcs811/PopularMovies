package com.jcaseydev.popularmovies.UI;


import android.content.Intent;
import android.content.SharedPreferences;
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

import com.jcaseydev.popularmovies.Backend.AsyncTaskCompleteListener;
import com.jcaseydev.popularmovies.Backend.ImageAdapter;
import com.jcaseydev.popularmovies.Backend.Movie;
import com.jcaseydev.popularmovies.R;
import com.jcaseydev.popularmovies.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by justinsMO on 2/24/2016.
 */
public class MovieListFragment extends Fragment {

    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private ArrayList<String> listOfMoviePosters = new ArrayList<>();
    private String MOVIE_KEY = "movie_info";
    private ImageAdapter imageAdapter;
    private String BASE_URL;
    private static final String KEY_INDEX = "index";

    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            BASE_URL = savedInstanceState.getString(KEY_INDEX);
        }
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (prefs.getBoolean("pref_sort", true)) {
            BASE_URL = "http://api.themoviedb.org/3/movie/now_playing";
            updateMovies(BASE_URL);
        } else {
            BASE_URL = "http://api.themoviedb.org/3/movie/top_rated";
            updateMovies(BASE_URL);
        }
    }

    private void updateMovies(String url) {
        //clear out both lists
        movieArrayList.clear();
        listOfMoviePosters.clear();

        //create a new async task and execute
        FetchMovieData fmd = new FetchMovieData();
        fmd.execute(url);
        //Toast.makeText(getContext(), "UpdateMoviesCalled" + url, Toast.LENGTH_SHORT).show();
        Log.d("URLTEST", url);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_INDEX, BASE_URL);
    }

    public class FetchMovieData extends AsyncTaskCompleteListener {

        @Override
        protected void onPostExecute(List<Movie> result) {
            if (result != null) {
                //add all of the movie objects that have be gathered
                //into the movieArrayList
                movieArrayList.addAll(result);
                //Call updatePoster to get movie posters
                updatePoster();

                //notify the image adapter that there is data now
                imageAdapter.notifyDataSetChanged();

            }
        }

        public void updatePoster() {
            //for every movie in movieArraylist add to
            //listOfMoviePosters
            for (Movie movie : movieArrayList) {
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

        if (id == R.id.settings_action) {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }
}
