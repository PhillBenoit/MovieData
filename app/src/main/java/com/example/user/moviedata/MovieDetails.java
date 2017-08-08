package com.example.user.moviedata;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

public class MovieDetails extends AppCompatActivity {

    String poster_url;
    int id;

    ImageView backdrop;
    TextView title, original_title, release_date, popularity, vote_average, overview;
    Button poster, reviews;

    Spinner trailers;
    ArrayAdapter<String> trailer_adapter;
    String[] spinner_items;
    String[] youtube_keys;

    //activity for viewing movie details
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        poster = (Button) findViewById(R.id.poster_button);
        reviews = (Button) findViewById(R.id.review_button);

        trailers = (Spinner) findViewById(R.id.trailer_spinner);
        youtube_keys = new String[]{""};
        spinner_items = new String[]{"Trailers Unavailable"};
        trailer_adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_dropdown_item, spinner_items);
        trailers.setAdapter(trailer_adapter);

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

            id = movie.getId();
            new getTrailers().execute();

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
                        .load(movie.getBackdrop()).placeholder(R.drawable.blank)
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

        //click listener for reviews
        reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewsClicked();
            }
        });

        //click listener for trailer
        trailers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) trailerClicked(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //enable back button
        android.app.ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);
    }

    //starts activity to view the poster
    private void posterClicked() {
        Context context = this;
        Class destination = ViewPoster.class;
        Intent intent = new Intent(context, destination);
        intent.putExtra(PublicStrings.poster_intent_item, poster_url);
        startActivity(intent);
    }

    //back button action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //starts activity that shows reviews
    private void reviewsClicked() {
        Context context = getBaseContext();
        Class destination = ReviewsActivity.class;
        Intent intent = new Intent(context, destination);
        intent.putExtra(PublicStrings.reviews_intent_item, id);
        startActivity(intent);
    }

    //launches intent for trailer
    private void trailerClicked(int index) {
        trailers.setSelection(0);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format
                (PublicStrings.base_youtube_url, youtube_keys[index]))));
    }

    public class getTrailers extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return MovieJSONUtils.getData(MovieJSONUtils.buildURL(id, "videos"));
        }

        @Override
        protected void onPostExecute(String s) {
            parseTrailers(s);
            trailer_adapter = new ArrayAdapter<String>
                    (getBaseContext(), android.R.layout.simple_spinner_dropdown_item, spinner_items);
            trailers.setAdapter(trailer_adapter);
        }

        void parseTrailers(String json_results) {
            //prevents loading empty data
            if (json_results == null) return;
            JSONObject search_object = null;
            try {
                search_object = new JSONObject(json_results);
                JSONArray array_results = search_object.getJSONArray("results");
                spinner_items = new String[array_results.length() + 1];
                youtube_keys = new String[array_results.length() + 1];
                spinner_items[0] = "Select Trailer";
                youtube_keys[0] = "";
                for (int counter = 0; counter < array_results.length(); counter++) {
                    JSONObject parsed_trailer = array_results.getJSONObject(counter);
                    spinner_items[counter+1] = parsed_trailer.getString("name");
                    youtube_keys[counter+1] = parsed_trailer.getString("key");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}