package com.jcaseydev.popularmovies.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jcaseydev.popularmovies.backend.Contract.MovieEntry;

/**
 * Created by justi on 5/20/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_MOVIES_TABLE = "CREATE TABLE IF NOT EXISTS" + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT," +
                MovieEntry.COLUMN_MOVIE_POSTER + " TEXT," +
                MovieEntry.COULMN_MOVIE_OVERVIEW + " TEXT," +
                MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT" +
                ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);

        onCreate(db);
    }

    public void addMovie(Movie movie){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(MovieEntry.COLUMN_MOVIE_TITLE, movie.getMovieTitle());
        values.put(MovieEntry.COULMN_MOVIE_OVERVIEW, movie.getMovieOverview());

        db.insert(MovieEntry.TABLE_NAME, null, values);
        db.close();
    }

    public Movie getMovie(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                MovieEntry.TABLE_NAME,
                new String[] { MovieEntry._ID, MovieEntry.COLUMN_MOVIE_TITLE, MovieEntry.COULMN_MOVIE_OVERVIEW},
                MovieEntry._ID + "=?",
                new String[] {String.valueOf(id)} , null , null, null);

        if (cursor != null && cursor.moveToFirst())
            cursor.move(id);
        return new Movie(cursor.getString(id));
    }
}
