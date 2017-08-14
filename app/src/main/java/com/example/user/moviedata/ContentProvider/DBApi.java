package com.example.user.moviedata.ContentProvider;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
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
 */

public class DBApi extends AppCompatActivity {
    public void addFavorite(MovieDataObject movie) {
        ContentValues cv = new ContentValues();

        cv.put(DBContract.Favorites.COLUMN_ID, movie.getId());

        cv.put(DBContract.Favorites.COLUMN_MOVIE_TITLE, movie.getMovie_title());
        cv.put(DBContract.Favorites.COLUMN_ORIGINAL_TITLE, movie.getOriginal_title());
        cv.put(DBContract.Favorites.COLUMN_OVERVIEW, movie.getOverview());
        cv.put(DBContract.Favorites.COLUMN_RELEASE_DATE, movie.getRelease_date());

        cv.put(DBContract.Favorites.COLUMN_POPULARITY, movie.getPopularity());
        cv.put(DBContract.Favorites.COLUMN_VOTE_AVERAGE, movie.getVote_average());

        cv.put(DBContract.Favorites.COLUMN_POSTER, getPicture(movie.getPoster()));
        cv.put(DBContract.Favorites.COLUMN_BACKDROP, getPicture(movie.getBackdrop()));

        Uri uri = getContentResolver().insert(DBContract.Favorites.CONTENT_URI, cv);

        if(uri != null) Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();

        finish();
    }

    private byte[] getPicture(String string_uri) {
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

    private URL buildURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addTempReview(String author, String review) {
        ContentValues cv = new ContentValues();

        cv.put(DBContract.TempReviews.COLUMN_AUTHOR, author);
        cv.put(DBContract.TempReviews.COLUMN_REVIEW, review);

        Uri uri = getContentResolver().insert(DBContract.TempReviews.CONTENT_URI, cv);
        if(uri != null) Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        finish();
    }
}
