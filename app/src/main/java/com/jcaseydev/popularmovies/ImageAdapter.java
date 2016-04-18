package com.jcaseydev.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter{

    ArrayList<String> posterPath;
    Context mContext;

    public ImageAdapter(Context context,ArrayList<String> posterPath){
        this.mContext = context;
        this.posterPath = posterPath;
    }

    @Override
    public int getCount() {
        return posterPath.size();
    }

    @Override
    public Object getItem(int position) {
        return posterPath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setAdjustViewBounds(true);
        imageView.setPadding(0, 0, 0, 0);
        Picasso.with(mContext)
                .load(posterPath.get(position))
                .into(imageView);
        return imageView;
    }
}
