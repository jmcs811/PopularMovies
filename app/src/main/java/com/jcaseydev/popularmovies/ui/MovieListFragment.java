package com.jcaseydev.popularmovies.ui;


import android.app.Activity;
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

import com.jcaseydev.popularmovies.backend.AsyncTaskCompleteListener;
import com.jcaseydev.popularmovies.backend.DatabaseHandler;
import com.jcaseydev.popularmovies.backend.ImageAdapter;
import com.jcaseydev.popularmovies.backend.Movie;
import com.jcaseydev.popularmovies.R;
import com.jcaseydev.popularmovies.SettingsActivity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by justinsMO on 2/24/2016.
 */
public class MovieListFragment extends Fragment {

    private ArrayList<Movie> movieArrayList = new ArrayList<>();
    private ArrayList<String> listOfMoviePosters = new ArrayList<>();
    private ImageAdapter imageAdapter;
    private String BASE_URL;
    private static final String KEY_INDEX = "index";
    private DatabaseHandler dbHandler;
    private Callbacks mCallbacks;

    public MovieListFragment() {
    }

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onMovieSelected(ArrayList movieList, int Position);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
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

                //uses onMovieSelected to determine if Tablet layout or phone is needed.
                mCallbacks.onMovieSelected(movieArrayList, position);
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

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_INDEX, BASE_URL);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
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
        } else if (id == R.id.favorite_action){
            Intent favs = new Intent(getContext(), FavoritesActivity.class);
            startActivity(favs);
        }
        return super.onOptionsItemSelected(item);

    }
}
