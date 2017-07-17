package com.example.user.moviedata;

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

    public static MovieDataObject[] parseJSON(String search_results) {
        MovieDataObject[] parsed_results;
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

                parsed_results[counter].setPoster(parsed_object.getString("poster_path"));
                parsed_results[counter].setBackdrop(parsed_object.getString("backdrop_path"));

                parsed_results[counter].setPopularity(parsed_object.getDouble("popularity"));
                parsed_results[counter].setVote_average(parsed_object.getDouble("vote_average"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            parsed_results = null;
        }

        return parsed_results;
    }

    public static URL buildURL(String base_url, String APIkey, String search) {
        try {
            return new URL(String.format(base_url, APIkey, search));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public static String getData(URL search_url) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) search_url.openConnection();
        InputStream in = connection.getInputStream();

        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        if (hasInput) {
            return scanner.next();
        } else {
            return null;
        }
    }
}
