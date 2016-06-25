package com.jcaseydev.popularmovies.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcaseydev.popularmovies.R;


/**
 * Created by justi on 5/22/2016.
 */
public class FavoritesFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorite_fragment, container, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());


        return rootView;
    }
}
