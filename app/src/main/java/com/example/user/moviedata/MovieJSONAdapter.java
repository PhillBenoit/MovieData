package com.example.user.moviedata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by User on 7/11/2017.
 */

public class MovieJSONAdapter {

    public MovieJSONAdapter() {

    }

    public MovieJSONAdapter(String raw_results) {
        parseJSON(raw_results);
    }

    private MovieDataObject[] parsed_results;

    public MovieDataObject[] getResults() {
        return  parsed_results;
    }

    class getPictures extends AsyncTask<String, Void, Void> {

        static final String base_image_url = "https://image.tmdb.org/t/p/w500";

        @Override
        protected Void doInBackground(String... params) {
            int index = Integer.parseInt(params[2]);

            Bitmap picture = getPicture(params[0]);
            if (picture != null)
                parsed_results[index].setPoster(picture.copy(picture.getConfig(), false));

            picture = getPicture(params[1]);
            if (picture != null)
                parsed_results[index].setBackdrop(picture.copy(picture.getConfig(), false));

            return null;
        }

        private Bitmap getPicture(String URL) {
            HttpURLConnection connection = null;
            InputStream stream;
            Bitmap picture = null;
            URL resource_url = buildURL(URL);
            if (resource_url == null) return null;

            try {
                connection = (HttpURLConnection) resource_url.openConnection();
                stream = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                stream = null;
            }

            if (stream != null) picture = BitmapFactory.decodeStream(stream);
            if (connection != null) connection.disconnect();
            return picture;
        }

        private URL buildURL(String resource_path) {
            try {
                return new URL(base_image_url + resource_path);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void parseJSON(String search_results) {

        try {
            JSONObject search_object = new JSONObject(search_results);
            JSONArray array = search_object.getJSONArray("results");
            parsed_results = new MovieDataObject[array.length()];

            for (int counter = 0; counter < array.length(); counter++) {
                JSONObject parsed_object = array.getJSONObject(counter);
                parsed_results[counter] = new MovieDataObject();

                parsed_results[counter].setMovie_title(parsed_object.getString("title"));
                parsed_results[counter].setOriginal_title(parsed_object.getString("original_title"));
                parsed_results[counter].setOverview(parsed_object.getString("overview"));
                parsed_results[counter].setRelease_date(parsed_object.getString("release_date"));

                new getPictures().execute(
                        parsed_object.getString("poster_path"),
                        parsed_object.getString("backdrop_path"),
                        Integer.toString(counter));

                parsed_results[counter].setPopularity(parsed_object.getDouble("popularity"));
                parsed_results[counter].setVote_average(parsed_object.getDouble("vote_average"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            parsed_results = null;
        }
    }

    public static URL buildURL(String base_url, String APIkey, String search) {
        try {
            return new URL(String.format(base_url, APIkey, search));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getData(URL search_url) {
        HttpURLConnection connection = null;
        String return_string = null;

        try {
            connection = (HttpURLConnection) search_url.openConnection();
            InputStream in = connection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) return_string = scanner.next();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (connection != null) connection.disconnect();
        return return_string;
    }
}
