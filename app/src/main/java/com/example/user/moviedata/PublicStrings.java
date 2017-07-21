package com.example.user.moviedata;

//strings used throughout the program
public class PublicStrings {
    public static final String

        //intent item names
            details_intent_item = "MOVIE_PARCEL",
            poster_intent_item = "EXTRA_STRING",

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
            base_search_url = "https://api.themoviedb.org/3/movie/%s?api_key=%s";
}
