package com.jcaseydev.popularmovies.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jcaseydev.popularmovies.R;

/**
 * Created by justi on 5/22/2016.
 */
public class FavoritesActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.favorites_container, new FavoritesFragment())
                    .commit();
        }
    }
}
