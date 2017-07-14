package com.example.user.moviedata;

import android.net.Uri;


/**
 * Created by User on 7/11/2017.
 */

public class MovieDataObject {

    public String movie_title, original_title, overview, release_date;
    public String poster, backdrop;
    public double popularity, vote_average;

    public MovieDataObject() {
        movie_title = "";
        original_title = "";
        overview = "";
        release_date = "";

        poster = "";
        backdrop = "";

        popularity = 0;
        vote_average = 0;
    }
}
