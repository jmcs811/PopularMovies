package com.jcaseydev.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailFragment extends Fragment{

    public DetailFragment(){}
    Movie movie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);

        //get intent
        Intent intent = getActivity().getIntent();

        //key for the intent extra
        String MOVIE_INFO = "movie_info";

        //test if intent is not null and if it has the correct extra
        if(intent != null && intent.hasExtra(MOVIE_INFO)){
            //fill movie with the details of the clicked item
            movie = intent.getParcelableExtra(MOVIE_INFO);

            //update view with all of the details
            updateView(rootView);

            Button trailerButton = (Button) rootView.findViewById(R.id.trailer_button);
            trailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_detail_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

                if (id == R.id.add_to_fav_action){
                    //TODO:add to favorites
                }

        return super.onOptionsItemSelected(item);
    }

    public class FetchMovieTrailers extends AsyncTask<Void, Void, Void> {

        private void getMovieUrl(){

        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}

