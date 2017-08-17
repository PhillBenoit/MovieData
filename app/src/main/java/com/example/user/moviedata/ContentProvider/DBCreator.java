package com.example.user.moviedata.ContentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 8/10/2017.
 * helper for crating the Database used by the content provider
 */

class DBCreator extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "movieDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    DBCreator(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    //creates tables as defined in the contract
    public void onCreate(SQLiteDatabase db) {
        String sql_command;

        sql_command = "CREATE TABLE "  + DBContract.Favorites.TABLE_NAME + " (" +
                DBContract.Favorites.COLUMN_ID             + " INTEGER PRIMARY KEY, " +
                DBContract.Favorites.COLUMN_MOVIE_TITLE    + " TEXT NOT NULL, " +
                DBContract.Favorites.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                DBContract.Favorites.COLUMN_OVERVIEW       + " TEXT, " +
                DBContract.Favorites.COLUMN_RELEASE_DATE   + " TEXT, " +
                DBContract.Favorites.COLUMN_POPULARITY     + " REAL NOT NULL, " +
                DBContract.Favorites.COLUMN_VOTE_AVERAGE   + " REAL NOT NULL, " +
                DBContract.Favorites.COLUMN_POSTER         + " BLOB, " +
                DBContract.Favorites.COLUMN_BACKDROP       + " BLOB);";
        db.execSQL(sql_command);

        sql_command = "CREATE TABLE "  + DBContract.FavoritesReviews.TABLE_NAME + " (" +
                DBContract.FavoritesReviews.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                DBContract.FavoritesReviews.COLUMN_AUTHOR   + " TEXT NOT NULL, " +
                DBContract.FavoritesReviews.COLUMN_REVIEW   + " TEXT NOT NULL, " +
                " FOREIGN KEY ("+ DBContract.FavoritesReviews.COLUMN_MOVIE_ID+") REFERENCES "
                + DBContract.Favorites.TABLE_NAME+"("+ DBContract.Favorites.COLUMN_ID+"));";
        db.execSQL(sql_command);

        sql_command = "CREATE TABLE "  + DBContract.TempReviews.TABLE_NAME + " (" +
                DBContract.TempReviews.COLUMN_AUTHOR         + " TEXT NOT NULL, " +
                DBContract.TempReviews.COLUMN_REVIEW         + " TEXT NOT NULL);";
        db.execSQL(sql_command);

    }

    @Override
    //updates database during schema changes
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.FavoritesReviews.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Favorites.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TempReviews.TABLE_NAME);
        onCreate(db);
    }
}
