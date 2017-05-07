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

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "movies.db";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_ID + " TEXT " +
                ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);

        onCreate(db);
    }

   public boolean addMovie(String item){
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put(MovieEntry.COLUMN_MOVIE_TITLE, item);

       Log.d("TAG", "addData: Adding " + item + " to " + MovieEntry.TABLE_NAME);

       long result = db.insert(MovieEntry.TABLE_NAME, null, contentValues);
       if (result == -1){
           return false;
       } else {
           return true;
       }
   }

    /**public Movie getMovie(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                MovieEntry.TABLE_NAME,
                new String[] { MovieEntry._ID, MovieEntry.COLUMN_MOVIE_TITLE, MovieEntry.COLUMN_MOVIE_ID},
                MovieEntry._ID + "=?",
                new String[] {String.valueOf(id)} , null , null, null);

        if (cursor != null && cursor.moveToFirst())
            cursor.move(id);
        return new Movie(cursor.getString(id));
    }**/

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + MovieEntry.TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public int getMovieCount(){
        String countQuery = "SELECT * FROM " + MovieEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        return cursor.getCount();
    }
}
