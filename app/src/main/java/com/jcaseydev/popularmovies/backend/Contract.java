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
        public static final String COLUMN_MOVIE_ID = "movieId";
    }
}
