package com.example.user.moviedata.ContentProvider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by User on 8/10/2017.
 */

public class DBContract {

    public static final String AUTHORITY = "com.example.user.moviedata";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Favorites implements BaseColumns {
        public static final String TABLE_NAME = "favorites";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_ID = "id";

        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BACKDROP = "backdrop";
    }

    private static class Reviews implements BaseColumns {
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_REVIEW = "review";
    }

    public static final class FavoritesReviews extends Reviews {
        public static final String TABLE_NAME = "favorites_reviews";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_MOVIE_ID = "movie_id";
    }

    public static final class TempReviews extends Reviews {
        public static final String TABLE_NAME = "temp_reviews";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
    }
}