package com.example.user.moviedata.ContentProvider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by User on 8/10/2017.
 * String values defining the database tables and fields
 */

public class DBContract {

    static final String AUTHORITY = "com.example.user.moviedata";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //holds data for favorite movies
    static final class Favorites implements BaseColumns {
        static final String TABLE_NAME = "favorites";

        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        static final String COLUMN_ID = "id";

        static final String COLUMN_MOVIE_TITLE = "movie_title";
        static final String COLUMN_ORIGINAL_TITLE = "original_title";
        static final String COLUMN_OVERVIEW = "overview";
        static final String COLUMN_RELEASE_DATE = "release_date";

        static final String COLUMN_POPULARITY = "popularity";
        static final String COLUMN_VOTE_AVERAGE = "vote_average";

        static final String COLUMN_POSTER = "poster";
        static final String COLUMN_BACKDROP = "backdrop";
    }

    //base class for reviews
    private static class Reviews implements BaseColumns {
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_REVIEW = "review";
    }

    //Reviews for movies found in the favorites table
    static final class FavoritesReviews extends Reviews {
        static final String TABLE_NAME = "favorites_reviews";

        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        static final String COLUMN_MOVIE_ID = "movie_id";
    }

    //temporary table to hold reviews used to cut back on network data calls
    public static final class TempReviews extends Reviews {
        static final String TABLE_NAME = "temp_reviews";

        static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
    }
}
