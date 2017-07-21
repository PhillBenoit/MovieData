package com.example.user.moviedata;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    ImageView backdrop;
    TextView title, original_title, release_date, popularity, vote_average, overview;
    Button poster;
    String poster_url;

    //activity for viewing movie details
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        poster = (Button) findViewById(R.id.poster_button);

        backdrop = (ImageView) findViewById(R.id.backdrop);

        title = (TextView) findViewById(R.id.title);
        original_title = (TextView) findViewById(R.id.original_title);
        release_date = (TextView) findViewById(R.id.release_date);
        popularity = (TextView) findViewById(R.id.popularity);
        vote_average = (TextView) findViewById(R.id.vote_average);
        overview = (TextView) findViewById(R.id.overview);

        Intent intent = getIntent();
        if (intent.hasExtra(PublicStrings.details_intent_item)) {
            String set_string;
            //unpacks movie data from the intent
            MovieDataObject movie = (MovieDataObject)
                    intent.getParcelableExtra(PublicStrings.details_intent_item);

            //direct sets
            title.setText(movie.getMovie_title());
            original_title.setText(movie.getOriginal_title());
            overview.setText(movie.getOverview());

            //sets with prefixes
            set_string = PublicStrings.release_date_prefix + movie.getRelease_date();
            release_date.setText(set_string);

            set_string = PublicStrings.popularity_prefix + Double.toString(movie.getPopularity());
            popularity.setText(set_string);

            set_string = PublicStrings.vote_average_prefix +
                    Double.toString(movie.getVote_average());
            vote_average.setText(set_string);

            //set backdrop image
            if (!MovieDataObject.equalsBaseURL(movie.getBackdrop())) {
                Picasso.with(this)
                        .load(movie.getBackdrop())
                        .into(backdrop);
            }

            //saves poster url to pass to poster view activity
            poster_url = movie.getPoster();
            //button is disabled if the poster is not available
            if (MovieDataObject.equalsBaseURL(poster_url)) {
                poster.setEnabled(false);
                poster.setText(PublicStrings.poster_not_available_message);
            }
        }

        //click listener for the poster button
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posterClicked();
            }
        });
    }

    //starts activity to view the poster
    private void posterClicked() {
        Context context = this;
        Class destination = ViewPoster.class;
        Intent intent = new Intent(context, destination);
        intent.putExtra(PublicStrings.poster_intent_item, poster_url);
        startActivity(intent);
    }
}
