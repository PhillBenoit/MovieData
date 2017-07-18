package com.example.user.moviedata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetails extends AppCompatActivity {

    ImageView poster, background;
    TextView title, original_title, release_date, popularity, vote_average, overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        poster = (ImageView) findViewById(R.id.poster);
        background = (ImageView) findViewById(R.id.background);

        title = (TextView) findViewById(R.id.title);
        original_title = (TextView) findViewById(R.id.original_title);
        release_date = (TextView) findViewById(R.id.release_date);
        popularity = (TextView) findViewById(R.id.popularity);
        vote_average = (TextView) findViewById(R.id.vote_average);
        overview = (TextView) findViewById(R.id.overview);

        Intent intent = getIntent();
        if (intent.hasExtra("MOVIE_PARCEL")) {
            String set_string;
            MovieDataObject movie = (MovieDataObject) intent.getParcelableExtra("MOVIE_PARCEL");
            title.setText(movie.getMovie_title());
            original_title.setText(movie.getOriginal_title());
            overview.setText(movie.getOverview());

            set_string = "Release Date: " + movie.getRelease_date();
            release_date.setText(set_string);

            set_string = "Popularity: " + Double.toString(movie.getPopularity());
            popularity.setText(set_string);

            set_string = "Vote Average: " + Double.toString(movie.getVote_average());
            vote_average.setText(set_string);
        }
    }
}
