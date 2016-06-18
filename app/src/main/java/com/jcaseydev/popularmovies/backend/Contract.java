package com.jcaseydev.popularmovies.backend;

import android.provider.BaseColumns;

/**
 * Created by justinsMO on 4/27/2016.
 */
public final class Contract {

    public Contract() {}

    public static abstract class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COULMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "date";
        public static final String COLUMN_MOVIE_VOTE  = "vote";
        public static final String COLUMN_MOVIE_ID = "movieId";
    }
}
