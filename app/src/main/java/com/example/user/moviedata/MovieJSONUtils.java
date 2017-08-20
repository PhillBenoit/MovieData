package com.example.user.moviedata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

//utilities for interacting with the movie database
public class MovieJSONUtils {

    public MovieJSONUtils() {

    }

    //parses movie data used by the rest of the program from string search results
    static MovieDataObject[] parseJSON(String search_results) {
        //prevents loading empty data
        if (search_results == null) return null;
        MovieDataObject[] parsed_results;
        try {
            //breaks the string up in to the main array
            JSONObject search_object = new JSONObject(search_results);
            JSONArray array = search_object.getJSONArray("results");
            parsed_results = new MovieDataObject[array.length()];

            //steps through the array to assign values
            for (int counter = 0; counter < array.length(); counter++) {
                JSONObject parsed_object = array.getJSONObject(counter);
                parsed_results[counter] = new MovieDataObject();

                //Strings
                parsed_results[counter].setMovie_title
                        (parsed_object.getString("title"));
                parsed_results[counter].setOriginal_title
                        (parsed_object.getString("original_title"));
                parsed_results[counter].setOverview
                        (parsed_object.getString("overview"));
                parsed_results[counter].setRelease_date
                        (parsed_object.getString("release_date"));

                //String urls for pictures
                parsed_results[counter].setPoster
                        (parsed_object.getString("poster_path"));
                parsed_results[counter].setBackdrop
                        (parsed_object.getString("backdrop_path"));

                //double precision floating points
                parsed_results[counter].setPopularity
                        (parsed_object.getDouble("popularity"));
                parsed_results[counter].setVote_average
                        (parsed_object.getDouble("vote_average"));

                //Integer containing id
                parsed_results[counter].setId(parsed_object.getInt("id"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            parsed_results = null;
        }
        return parsed_results;
    }

    //builds the search url used to get results
    static URL buildURL(String search_type) {
        try {
            return new URL(String.format
                    (PublicStrings.base_search_url,
                            search_type,
                            PrivateStrings.api_key));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL buildURL(Integer id, String search_type) {
        try {
            return new URL(String.format(Locale.ENGLISH,
                    PublicStrings.base_details_url,
                    id, search_type,
                    PrivateStrings.api_key));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //task used to connect to the database
    public static String getData(URL search_url) {
        HttpURLConnection connection = null;
        String return_string = null;

        try {
            connection = (HttpURLConnection) search_url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
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
