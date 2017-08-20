package com.example.user.moviedata;

import android.content.Context;

//strings used throughout the program
public class PublicStrings {

    private static String getFromRes(int id, Context c) {
        return c.getString(id);
    }

    public static String BASE_IMAGE_URL(Context c)
    {return getFromRes(R.string.picture_url, c);}

    public static final String

        //intent item names
            details_intent_item = "MOVIE_PARCEL",
            poster_intent_item_url = "POSTER_URL",
            poster_intent_item_id = "MOVIE_ID",
            reviews_intent_item = "ID",

        //favorites labels
            add_favorite = "Add To Favorites",
            remove_favorite = "Remove From Favorites",

        //output for details
            release_date_prefix = "Release Date: ",
            popularity_prefix = "Popularity: ",
            vote_average_prefix = "Vote Average: ",
            poster_not_available_message = "POSTER NOT AVAILABLE",

        //Used in MovieDataObject to start all url image returns
            base_image_url = "https://image.tmdb.org/t/p/w500",
        //Used to help find null URLs
            null_string = "null",

        //Base search URL used to build search queries
            base_search_url = "https://api.themoviedb.org/3/movie/%s?api_key=%s",

        //Base reviews/trailers URL
            base_details_url = "https://api.themoviedb.org/3/movie/%d/%s?api_key=%s&language=en-US",

        //base youtube url
            base_youtube_url = "http://www.youtube.com/watch?v=%s";
}
