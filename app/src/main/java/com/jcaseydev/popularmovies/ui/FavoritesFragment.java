package com.jcaseydev.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcaseydev.popularmovies.R;
import com.jcaseydev.popularmovies.backend.DatabaseHandler;


/**
 * Created by justi on 5/22/2016.
 */
public class FavoritesFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorite_fragment, container, false);

        DatabaseHandler db = new DatabaseHandler(getContext());
        db.getReadableDatabase();

        TextView movie1, movie2, movie3;
        movie1 = (TextView)rootView.findViewById(R.id.movie1);
        movie2 = (TextView)rootView.findViewById(R.id.movie2);
        movie3 = (TextView)rootView.findViewById(R.id.movie3);

        movie1.setText(db.getMovie(1).getMovieTitle());
        movie2.setText(db.getMovie(2).getMovieTitle());
        movie3.setText(db.getMovie(3).getMovieTitle());


        return rootView;
    }
}
