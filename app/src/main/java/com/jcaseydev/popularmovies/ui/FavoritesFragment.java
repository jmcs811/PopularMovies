package com.jcaseydev.popularmovies.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcaseydev.popularmovies.R;
import com.jcaseydev.popularmovies.backend.DatabaseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by justi on 5/22/2016.
 */
public class FavoritesFragment extends Fragment{

    private DatabaseHandler favoritesDb;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> listOfFavMovies = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favorite_fragment, container, false);
        favoritesDb = new DatabaseHandler(getContext());
        populateListView();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.favorites_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerAdapter(listOfFavMovies);
        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }

    private void populateListView() {

        Cursor data = favoritesDb.getData();
        ArrayList<String> listdata = new ArrayList<>();
        while (data.moveToNext()) {
            listOfFavMovies.add(data.getString(1));
        }
    }

        class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.FavViewHolder> {
            private List<String> mfavMovies;

            class FavViewHolder extends RecyclerView.ViewHolder {

                private TextView mFavTextView;

                public FavViewHolder(View itemView) {
                    super(itemView);
                    mFavTextView = (TextView) itemView.findViewById(R.id.favs_list_item);
                }
            }

            RecyclerAdapter(ArrayList<String> favs) {
                mfavMovies = favs;
            }

            RecyclerAdapter(List<String> favMovies) {
                mfavMovies = favMovies;
            }

            @Override
            public FavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View inflateView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.favorites_list_item, parent, false);
                return new FavViewHolder(inflateView);
            }

            @Override
            public void onBindViewHolder(FavViewHolder holder, int position) {
                holder.mFavTextView.setText(mfavMovies.get(position).toString());
            }

            @Override
            public int getItemCount() {
                return mfavMovies.size();
            }
        }
    }
