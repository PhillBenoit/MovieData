package com.example.user.moviedata.ContentProvider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.moviedata.MovieDataObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by User on 8/11/2017.
 * API for content provider
 */

public class DBApi {

    //adds to the favorites table
    static public void addFavorite(MovieDataObject movie, Context c) {
        ContentValues cv = new ContentValues();

        cv.put(DBContract.Favorites.COLUMN_ID, movie.getId());

        cv.put(DBContract.Favorites.COLUMN_MOVIE_TITLE, movie.getMovie_title());
        cv.put(DBContract.Favorites.COLUMN_ORIGINAL_TITLE, movie.getOriginal_title());
        cv.put(DBContract.Favorites.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(DBContract.Favorites.COLUMN_RELEASE_DATE, movie.getRelease_date());

        cv.put(DBContract.Favorites.COLUMN_POPULARITY, movie.getPopularity());
        cv.put(DBContract.Favorites.COLUMN_VOTE_AVERAGE, movie.getVote_average());

        //pictures are loaded as byte arrays
        cv.put(DBContract.Favorites.COLUMN_POSTER,
                getPictureFromInternet(movie.getPoster()));
        cv.put(DBContract.Favorites.COLUMN_BACKDROP,
                getPictureFromInternet(movie.getBackdrop()));

        Uri uri = c.getContentResolver().insert(DBContract.Favorites.CONTENT_URI, cv);

        if(uri != null) Toast.makeText(c, uri.toString(), Toast.LENGTH_LONG).show();

        //call to add to favorites reviews table
        addFavoritesReviews(movie.getId(), c);
    }

    //returns favorites as an array of movie data objects
    static public MovieDataObject[] getFavorites(Context c) {
        Cursor cursor = c.getContentResolver().query
                (DBContract.Favorites.CONTENT_URI, null, null, null, null);
        if (cursor == null) return null;
        int movie_count = cursor.getCount(), counter = 0;
        MovieDataObject[] movies = new MovieDataObject[movie_count];
        if (movie_count > 0) {
            int index_title = cursor.getColumnIndex
                    (DBContract.Favorites.COLUMN_MOVIE_TITLE);
            int index_original_title = cursor.getColumnIndex
                    (DBContract.Favorites.COLUMN_ORIGINAL_TITLE);
            int index_overview = cursor.getColumnIndex
                    (DBContract.Favorites.COLUMN_OVERVIEW);
            int index_release_date = cursor.getColumnIndex
                    (DBContract.Favorites.COLUMN_RELEASE_DATE);
            int index_popularity = cursor.getColumnIndex
                    (DBContract.Favorites.COLUMN_POPULARITY);
            int index_vote_average = cursor.getColumnIndex
                    (DBContract.Favorites.COLUMN_VOTE_AVERAGE);
            int index_id = cursor.getColumnIndex(DBContract.Favorites.COLUMN_ID);
            cursor.moveToFirst();
            do {
                movies[counter] = new MovieDataObject();
                movies[counter].setId
                        (cursor.getInt(index_id));
                movies[counter].setVote_average
                        (cursor.getDouble(index_vote_average));
                movies[counter].setPopularity
                        (cursor.getDouble(index_popularity));
                movies[counter].setRelease_date
                        (cursor.getString(index_release_date));
                movies[counter].setOverview
                        (cursor.getString(index_overview));
                movies[counter].setOriginal_title
                        (cursor.getString(index_original_title));
                movies[counter].setMovie_title
                        (cursor.getString(index_title));
                counter++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        return movies;
    }

    //adds to the favorites reviews table
    static private void addFavoritesReviews(Integer id, Context c) {
        Cursor cursor = getReviewsCursor(0, c);
        if (cursor.getCount() == 0) return;
        int author_column_index, review_column_index;
        Integer counter = 0;
        author_column_index = cursor.getColumnIndex
                (DBContract.TempReviews.COLUMN_AUTHOR);
        review_column_index = cursor.getColumnIndex
                (DBContract.TempReviews.COLUMN_REVIEW);
        cursor.moveToFirst();
        //loops to add data from temp table
        do {
            ContentValues cv = new ContentValues();
            cv.put(DBContract.FavoritesReviews.COLUMN_MOVIE_ID,
                    id);
            cv.put(DBContract.FavoritesReviews.COLUMN_AUTHOR,
                    cursor.getString(author_column_index));
            cv.put(DBContract.FavoritesReviews.COLUMN_REVIEW,
                    cursor.getString(review_column_index));
            Uri uri = c.getContentResolver().insert
                    (DBContract.FavoritesReviews.CONTENT_URI, cv);
            if (uri != null) counter++;
        } while (cursor.moveToNext());
        Toast.makeText(
                c, counter.toString() +
                        " reviews added for favorite movie with id " +
                        id.toString(),
                Toast.LENGTH_SHORT).show();
        cursor.close();
    }

    //removes from favorites table
    static public void removeFavorite(Integer id, Context c) {
        //call to delete the reviews before deleting the entry
        Integer n = c.getContentResolver().delete
                (DBContract.FavoritesReviews.CONTENT_URI,
                DBContract.FavoritesReviews.COLUMN_MOVIE_ID,
                new String[]{id.toString()});
        int o = c.getContentResolver().delete(DBContract.Favorites.CONTENT_URI,
                DBContract.Favorites.COLUMN_ID,
                new String[]{id.toString()});
        if (o > 0) Toast.makeText
                (c, "Movie with " + n.toString() + " reviews removed",
                        Toast.LENGTH_SHORT).show();
    }

    //loads picture data from the internet
    static private byte[] getPictureFromInternet(String string_uri) {
        URL resource_url = buildURL(string_uri);
        ByteArrayOutputStream o = new ByteArrayOutputStream();

        try {
            URLConnection c;

            if (resource_url != null) c = resource_url.openConnection();
            else return null;
            c.setConnectTimeout(5000);
            c.setReadTimeout(10000);

            InputStream i = resource_url.openStream();
            byte[] buffer = new byte[4096];
            int buffer_step;

            while ((buffer_step = i.read(buffer, 0, buffer.length)) != -1)
                o.write(buffer, 0, buffer_step);

            o.flush();
            i.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return o.toByteArray();
    }

    //builds url from string for retrieving data from the internet
    static private URL buildURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //adds to temp review table
    static public void addTempReview(String author, String review, Context c) {
        ContentValues cv = new ContentValues();

        cv.put(DBContract.TempReviews.COLUMN_AUTHOR, author);
        cv.put(DBContract.TempReviews.COLUMN_REVIEW, review);

        Uri uri = c.getContentResolver().insert
                (DBContract.TempReviews.CONTENT_URI, cv);
        if(uri != null) Toast.makeText(c, uri.toString(),
                Toast.LENGTH_LONG).show();
    }

    //clears the temp table
    static public void deleteTemp(Context c) {
        Integer n = c.getContentResolver().delete
                (DBContract.TempReviews.CONTENT_URI, null, null);
        if (n > 0) Toast.makeText
                (c, n.toString() + " rows removed from temp table",
                        Toast.LENGTH_SHORT).show();
    }

    //gets a cursor for reading reviews
    //if passed id = 0, it gets them from temp table
    //else it gets them from favorites reviews with the appropriate id
    static public Cursor getReviewsCursor(Integer id, Context c) {
        Cursor cursor;
        if (id == 0) cursor = c.getContentResolver().query
                (DBContract.TempReviews.CONTENT_URI, null, null, null, null);
        else cursor = c.getContentResolver().query(
                DBContract.FavoritesReviews.CONTENT_URI,
                null,
                DBContract.FavoritesReviews.COLUMN_MOVIE_ID + "=" + id.toString(),
                null,
                null);
        return cursor;
    }

    //boolean signifying if the given id exists in the favorites
    static public boolean favoriteExist(Integer id, Context c) {
        Cursor cursor = c.getContentResolver().query(
                DBContract.Favorites.CONTENT_URI,
                null,
                DBContract.Favorites.COLUMN_ID + "=" + id.toString(),
                null,
                null);
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count != 0;
    }

    //boolean signifying if the favorites reviews table has any entries for the given id
    static public boolean favoriteReviewExist(Integer id, Context c) {
        Cursor cursor = c.getContentResolver().query(
                DBContract.FavoritesReviews.CONTENT_URI,
                null,
                DBContract.FavoritesReviews.COLUMN_MOVIE_ID + "=" + id.toString(),
                null,
                null);
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count != 0;
    }

    //boolean signifying if a poster has been saved to the database
    static public boolean hasPoster(Integer id, Context c) {
        Cursor cursor = c.getContentResolver().query(
                DBContract.Favorites.CONTENT_URI,
                null,
                DBContract.Favorites.COLUMN_ID + "=" + id.toString(),
                null,
                null);
        byte[] result = null;
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            result = cursor.getBlob
                    (cursor.getColumnIndex(DBContract.Favorites.COLUMN_POSTER));
        }
        if (cursor != null) cursor.close();
        return result != null;
    }

    //loads an imageview with data from the poster field
    static public void getPoster(int id, ImageView i, Context c) {
        i.setImageBitmap(getImage(DBContract.Favorites.COLUMN_POSTER, id, c));
    }

    //loads an imageview with data from the backdrop field
    static public void getBackdrop(int id, ImageView i, Context c) {
        i.setImageBitmap(getImage(DBContract.Favorites.COLUMN_BACKDROP, id, c));
    }

    //returns a bitmap as decoded from the given field of an entry with matching id
    static private Bitmap getImage(String item_type, Integer id, Context c) {
        Bitmap return_bitmap = null;
        Cursor cursor = c.getContentResolver().query(
                DBContract.Favorites.CONTENT_URI,
                null,
                DBContract.Favorites.COLUMN_ID + "=" + id.toString(),
                null,
                null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            byte[] result = cursor.getBlob(cursor.getColumnIndex(item_type));
            return_bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
        }
        if (cursor != null) cursor.close();
        return return_bitmap;
    }
}
