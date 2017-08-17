package com.example.user.moviedata.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by User on 8/11/2017.
 * Content provider for the App's database
 */


public class MovieContentProvider extends ContentProvider {

    public static final int FAVORITES = 100;

    public static final int FAVORITES_REVIEWS = 200;

    public static final int TEMP = 300;

    private static final UriMatcher uri_matcher = buildUriMatcher();

    //builds matcher for URIs
    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.Favorites.TABLE_NAME, FAVORITES);

        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.FavoritesReviews.TABLE_NAME, FAVORITES_REVIEWS);

        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.TempReviews.TABLE_NAME, TEMP);

        return uriMatcher;
    }

    private DBCreator db_tables;

    @Override
    public boolean onCreate() {
        db_tables = new DBCreator(getContext());
        return true;
    }

    @Nullable
    @Override
    //queries the database
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = db_tables.getReadableDatabase();
        int match = uri_matcher.match(uri);
        Cursor return_cursor;
        String table_name;

        //forks selecting the appropriate table
        switch (match) {
            case FAVORITES:
                table_name = DBContract.Favorites.TABLE_NAME;
                break;
            case FAVORITES_REVIEWS:
                table_name = DBContract.FavoritesReviews.TABLE_NAME;
                break;
            case TEMP:
                table_name = DBContract.TempReviews.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return_cursor =  db.query(table_name,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        Context c = getContext();
        if (c != null) return_cursor.setNotificationUri(c.getContentResolver(), uri);

        return return_cursor;
    }

    @Nullable
    @Override
    //adds rows to database
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = db_tables.getWritableDatabase();
        int match = uri_matcher.match(uri);
        Uri return_uri;
        String table_name;

        //forks selecting the appropriate table
        switch (match) {
            case FAVORITES:
                table_name = DBContract.Favorites.TABLE_NAME;
                break;

            case FAVORITES_REVIEWS:
                table_name = DBContract.FavoritesReviews.TABLE_NAME;
                break;

            case TEMP:
                table_name = DBContract.TempReviews.TABLE_NAME;
                break;

            default:
                throw new UnsupportedOperationException
                        ("Content Provider did not recognize uri: " + uri);
        }

        long id = db.insert(table_name, null, values);
        if ( id > 0 ) return_uri = ContentUris.withAppendedId(uri, id);
        else throw new android.database.SQLException("Failed to insert row into " + table_name);

        Context c = getContext();
        if (c != null) c.getContentResolver().notifyChange(uri, null);
        return return_uri;
    }

    @Override
    //removes from tables
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = db_tables.getWritableDatabase();
        int match = uri_matcher.match(uri);
        int tasksDeleted;
        String table;

        //forks selecting the appropriate table
        //postpend selection if needed for tables that remove by id
        switch (match) {
            case FAVORITES:
                table = DBContract.Favorites.TABLE_NAME;
                selection = selection + "=?";
                break;

            case FAVORITES_REVIEWS:
                table = DBContract.FavoritesReviews.TABLE_NAME;
                selection = selection + "=?";
                break;

            case TEMP:
                table = DBContract.TempReviews.TABLE_NAME;
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        tasksDeleted = db.delete(table, selection, selectionArgs);

        Context c = getContext();
        if (tasksDeleted > 0 && c != null) c.getContentResolver().notifyChange(uri, null);

        return tasksDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
