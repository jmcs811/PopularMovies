package com.jcaseydev.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailFragment extends Fragment{
    public DetailFragment(){}
    Movie movie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);

        //get intent
        Intent intent = getActivity().getIntent();

        //key for the intent extra
        String MOVIE_INFO = "movie_info";

        //test if intent is null and if it has the correct extra
        if(intent != null && intent.hasExtra(MOVIE_INFO)){
            //fill movie with the details of the clicked item
            movie = intent.getParcelableExtra(MOVIE_INFO);

            //update view with all of the details
            updateView(rootView);
        }

        return rootView;
    }
    private void updateView(View view){
        //set up the views
        TextView title = (TextView) view.findViewById(R.id.movieTitle);
        TextView overview = (TextView)view.findViewById(R.id.movieOverview);
        TextView releaseDate = (TextView)view.findViewById(R.id.movieReleaseDate);
        ImageView moviePoster = (ImageView)view.findViewById(R.id.moviePoster);
        TextView movieVoteAvg = (TextView)view.findViewById(R.id.movieVote);


        //set the text on the views
        title.setText(movie.getMovieTitle());
        overview.setText(movie.getMovieOverview());
        releaseDate.setText(movie.getMovieReleaseDate());
        Picasso.with(getActivity()).load(movie.getMoviePoster()).into(moviePoster);
        movieVoteAvg.setText(Double.toString(movie.getMovieVoteAverage()));
    }
}
