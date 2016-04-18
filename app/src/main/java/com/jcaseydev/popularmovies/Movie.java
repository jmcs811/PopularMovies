package com.jcaseydev.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by justinsMO on 2/27/2016.
 */
public class Movie implements Parcelable{
    private String movieTitle;
    private String movieOverview;
    private String movieReleaseDate;
    private String moviePoster;
    private double movieVoteAverage;
    private int movieId;

    public Movie(String title,String poster, String overview, String releasedate, double movieVoteAverage, int movieId) {
        this.movieTitle = title;
        this.moviePoster = poster;
        this.movieOverview = overview;
        this.movieReleaseDate = releasedate;
        this.movieVoteAverage = movieVoteAverage;
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public double getMovieVoteAverage() {
        return movieVoteAverage;
    }

    public double getMovieId(){
        return movieId;
    }

    public Movie(Parcel in) {
        movieTitle = in.readString();
        moviePoster = in.readString();
        movieOverview = in.readString();
        movieReleaseDate = in.readString();
        movieVoteAverage = in.readDouble();
        movieId = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            Log.d("create from parcel", "Created from Parcel");
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieTitle);
        dest.writeString(moviePoster);
        dest.writeString(movieOverview);
        dest.writeString(movieReleaseDate);
        dest.writeDouble(movieVoteAverage);
        dest.writeInt(movieId);
    }
}