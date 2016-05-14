package com.jcaseydev.popularmovies.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jcaseydev.popularmovies.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.detail_container);
            if (fragment == null){
                fragment = new MovieListFragment();
                fm.beginTransaction()
                        .add(R.id.detail_container, fragment)
                        .commit();
            }


    }
}
