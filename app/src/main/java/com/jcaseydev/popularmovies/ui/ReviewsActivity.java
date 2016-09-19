package com.jcaseydev.popularmovies.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.jcaseydev.popularmovies.R;

/**
 * Created by justi on 5/17/2016.
 */
public class ReviewsActivity extends SingleFragmentActivity{


    @Override
    protected Fragment createFragment() {
        return new ReviewsFragment();
    }
}
