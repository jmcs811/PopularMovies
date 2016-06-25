package com.jcaseydev.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jcaseydev.popularmovies.BuildConfig;
import com.jcaseydev.popularmovies.R;
import com.jcaseydev.popularmovies.backend.Reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by justi on 5/17/2016.
 */
public class ReviewsFragment extends Fragment{

    public ReviewsFragment(){}
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager linearLayoutManager;
    RecyclerView.Adapter mAdapter;
    private int movieId;
    private List<Reviews> movieReviews = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reviews_fragment, container, false);

        FetchMovieReviews fmr = new FetchMovieReviews();
        fmr.execute();

        //get intent
        Intent intent = getActivity().getIntent();

        //key for the intent extra
        String MOVIE_ID = "movie_id";
        if(intent != null && intent.hasExtra(MOVIE_ID)){
           movieId = intent.getIntExtra(MOVIE_ID, 49026);
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.reviews_recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new MyAdapter(movieReviews);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);



    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        private List<Reviews> mDataset;

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView mAuthorTextView, mReviewTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mAuthorTextView = (TextView) itemView.findViewById(R.id.author_text_view);
                mReviewTextView = (TextView) itemView.findViewById(R.id.review_text_view);
            }
        }

        public MyAdapter(List<Reviews> reviews){
            mDataset = reviews;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            holder.mAuthorTextView.setText(mDataset.get(position).getAuthor());
            holder.mReviewTextView.setText(mDataset.get(position).getContent());
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public class FetchMovieReviews extends AsyncTask<Void, Void, List<Reviews>> {

        private List<Reviews> getMovieReviews(String json) throws JSONException {
            final String JSON_ARRAY = "results";
            final String REVIEW_AUTHOR = "author";
            final String REVIEW_CONTENT = "content";

            JSONObject jsonObject = new JSONObject(json);

            JSONArray reviewArray = jsonObject.getJSONArray(JSON_ARRAY);

            int limit = reviewArray.length();
            List<Reviews> reviews = new ArrayList<>();

            for(int i = 0; i < limit; i++){
                JSONObject reviewObject = reviewArray.getJSONObject(i);

                String author = reviewObject.getString(REVIEW_AUTHOR);
                String content = reviewObject.getString(REVIEW_CONTENT);

               // Log.d("AUTHOR and CONTENT", author + " said " + content);
                reviews.add(new Reviews(author, content));
            }

            return reviews;
        }

        @Override
        protected List<Reviews> doInBackground(Void... params) {

            final String QUERY_API = "api_key";
            final String QUERY_URL = "http://api.themoviedb.org/3/movie/" + movieId + "/reviews";

                    //Build URL to get data from
            Uri builtUri = Uri.parse(QUERY_URL).buildUpon()
                    .appendQueryParameter(QUERY_API, BuildConfig.TMDB_API_KEY)
                    .build();

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(builtUri.toString()).build();


            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                String jsonData = response.body().string();
                return getMovieReviews(jsonData);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Reviews> reviews) {
            super.onPostExecute(reviews);

            if(reviews != null){
                movieReviews.addAll(reviews);
                mAdapter.notifyDataSetChanged();
            }
        }
    }


}
