package com.jcaseydev.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jcaseydev.popularmovies.R;
import com.jcaseydev.popularmovies.backend.Movie;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends SingleFragmentActivity implements MovieListFragment.Callbacks{

    private String MOVIE_KEY = "movie_info";
   // int position;
   // ArrayList movieArrayList;

    @Override
    protected Fragment createFragment() {
        return new MovieListFragment();
    }

    @Override
    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onMovieSelected(ArrayList movieList, int Position) {
        if(findViewById(R.id.detail_fragment_container) == null){

            Movie details = (Movie) movieList.get(Position);
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(MOVIE_KEY, details);
            startActivity(intent);
        } else {
            Fragment newDetail = new DetailFragment();
            Bundle detailInfo = new Bundle();
            detailInfo.putParcelableArrayList("detail_info", movieList);
            detailInfo.putInt("POSITION", Position);
            newDetail.setArguments(detailInfo);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    public boolean isInternetAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()){
            try{
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1000);
                urlc.connect();
                if (urlc.getResponseCode() == 200){
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                Log.i("Warning", "Error Checking internet connection", e);
                return false;
            }
        }
        return false;
    }
}
