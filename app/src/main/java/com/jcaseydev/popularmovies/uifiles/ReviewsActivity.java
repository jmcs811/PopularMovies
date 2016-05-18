package com.jcaseydev.popularmovies.uifiles;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.jcaseydev.popularmovies.R;

/**
 * Created by justi on 5/17/2016.
 */
public class ReviewsActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.review_activity);
        if (fragment == null){
            fragment = new ReviewsFragment();
            fm.beginTransaction()
                    .add(R.id.review_activity, fragment)
                    .commit();
        }
    }
}
