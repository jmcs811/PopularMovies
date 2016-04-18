package com.jcaseydev.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class DetailFragment extends Fragment{

    public DetailFragment(){}
    Movie movie;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);

        Intent intent = getActivity().getIntent();
        String MOVIE_INFO = "movie_info";
        if(intent != null && intent.hasExtra(MOVIE_INFO)){
            movie = intent.getParcelableExtra(MOVIE_INFO);
            updateView(rootView);
        }

        return rootView;
    }
    private void updateView(View view){
        TextView title = (TextView) view.findViewById(R.id.movieTitle);
        TextView overview = (TextView)view.findViewById(R.id.movieOverview);
        TextView releaseDate = (TextView)view.findViewById(R.id.movieReleaseDate);
        ImageView moviePoster = (ImageView)view.findViewById(R.id.moviePoster);
        TextView movieVoteAvg = (TextView)view.findViewById(R.id.movieVote);

        title.setText(movie.getMovieTitle());
        overview.setText(movie.getMovieOverview());
        releaseDate.setText(movie.getMovieReleaseDate());
        Picasso.with(getActivity()).load(movie.getMoviePoster()).into(moviePoster);


        String test = Double.toString(movie.getMovieVoteAverage());
        Log.d("TEST", test);
        movieVoteAvg.setText(test);
    }
}
