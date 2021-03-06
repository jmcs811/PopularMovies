package com.jcaseydev.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcaseydev.popularmovies.backend.Contract;
import com.jcaseydev.popularmovies.backend.DatabaseHandler;
import com.jcaseydev.popularmovies.backend.Movie;
import com.jcaseydev.popularmovies.BuildConfig;
import com.jcaseydev.popularmovies.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DetailFragment extends Fragment {

    public DetailFragment() {
    }

    private Movie movie;
    private String trailerUrl;
    private final static String MOVIE_ID = "movie_id";
    private DatabaseHandler favoritesDb;
    private ArrayList movieList;
    int position;
    private FetchMovieTrailers fmt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        favoritesDb = new DatabaseHandler(getContext());

        Bundle detailInfo = this.getArguments();
        if (detailInfo != null) {
            movieList = detailInfo.getParcelableArrayList("detail_info");
            position = detailInfo.getInt("POSITION");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);

        //get intent
        Intent intent = getActivity().getIntent();

        //key for the intent extra
        String MOVIE_INFO = "movie_info";

        //test if intent is not null and if it has the correct extra
        if (intent != null && intent.hasExtra(MOVIE_INFO)) {
            //fill movie with the details of the clicked item
            movie = intent.getParcelableExtra(MOVIE_INFO);

            //update view with all of the details
            updateView(rootView);

            fmt = new FetchMovieTrailers();
            fmt.execute();

            final Button trailerButton = (Button) rootView.findViewById(R.id.trailer_button);
            trailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Uri video = Uri.parse(trailerUrl);
                        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, video);
                        startActivity(trailerIntent);
                    } catch (NullPointerException e) {
                        Toast.makeText(getContext(), "Sorry no trailer yet", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            movie = (Movie) movieList.get(position);
            updateView(rootView);
            fmt = new FetchMovieTrailers();
            fmt.execute();

            final Button trailerButton = (Button) rootView.findViewById(R.id.trailer_button);
            trailerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Uri video = Uri.parse(trailerUrl);
                        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, video);
                        startActivity(trailerIntent);
                    } catch (NullPointerException e) {
                        Toast.makeText(getContext(), "Sorry no trailer yet", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        Button reviewsButton = (Button) rootView.findViewById(R.id.reviews_button);
        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReviewsActivity.class)
                        .putExtra(MOVIE_ID, movie.getMovieId());
                startActivity(intent);

            }
        });

        return rootView;
    }

    private void updateView(View view) {
        //set up the views
        TextView title = (TextView) view.findViewById(R.id.movieTitle);
        TextView overview = (TextView) view.findViewById(R.id.movieOverview);
        TextView releaseDate = (TextView) view.findViewById(R.id.movieReleaseDate);
        ImageView moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
        TextView movieVoteAvg = (TextView) view.findViewById(R.id.movieVote);


        //set the text on the views
        title.setText(movie.getMovieTitle());
        overview.setText("Synopsis: " + movie.getMovieOverview());
        releaseDate.setText("Release Date: " + movie.getMovieReleaseDate());
        Picasso.with(getActivity()).load(movie.getMoviePoster()).into(moviePoster);
        movieVoteAvg.setText("Movie Rating: " + Double.toString(movie.getMovieVoteAverage()));
    }

    public void addData(Movie movie) {
        boolean insertData = favoritesDb.addMovie(movie.getMovieTitle());

        if (insertData){
            Toast.makeText(getContext(), "Data Successfully Inserted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_detail_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_to_fav_action) {
            addData(movie);

        }

        return super.onOptionsItemSelected(item);
    }


    //Start of the async task to get movie trailer
    public class FetchMovieTrailers extends AsyncTask<Void, Void, String> {

        private String getMovieUrl(String json) throws JSONException {

            final String JSON_ARRAY = "results";
            final String TRAILER_KEY = "key";
            String movieKey;

            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray(JSON_ARRAY);

            JSONObject keyObject = jsonArray.getJSONObject(0);
            movieKey = keyObject.getString(TRAILER_KEY);
            return movieKey;
        }

        @Override
        protected String doInBackground(Void... voids) {
            final String QUERY_API = "api_key";
            final String QUERY_URL = "http://api.themoviedb.org/3/movie/" + movie.getMovieId() + "/videos";

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
                return getMovieUrl(jsonData);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                trailerUrl = "https://www.youtube.com/watch?v=" + s;
            }
        }
    }
}

