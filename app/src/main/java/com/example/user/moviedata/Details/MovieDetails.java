package com.example.user.moviedata.Details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.user.moviedata.ContentProvider.DBApi;
import com.example.user.moviedata.MovieDataObject;
import com.example.user.moviedata.MovieJSONUtils;
import com.example.user.moviedata.PublicStrings;
import com.example.user.moviedata.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetails extends AppCompatActivity {

    MovieDataObject movie;

    ImageView backdrop;
    TextView title, original_title, release_date,
            popularity, vote_average, overview;
    Button poster, reviews, favorites;

    Spinner trailers;
    ArrayAdapter<String> trailer_adapter;
    String[] spinner_items;
    String[] youtube_keys;

    //activity for viewing movie details
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //set up buttons
        poster = (Button) findViewById(R.id.poster_button);
        reviews = (Button) findViewById(R.id.review_button);
        favorites = (Button) findViewById(R.id.favorites_button);

        //set up spinner for trailers
        trailers = (Spinner) findViewById(R.id.trailer_spinner);
        youtube_keys = new String[]{""};
        spinner_items = new String[]{"Trailers Unavailable"};
        trailer_adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinner_items);
        trailers.setAdapter(trailer_adapter);

        //backdrop image
        backdrop = (ImageView) findViewById(R.id.backdrop);

        //textviews
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
            movie = intent.getParcelableExtra(PublicStrings.details_intent_item);

            new getTrailers().execute();

            //forking logic to load either from
            //favorites table or from the network
            int id = movie.getId();
            if (DBApi.favoriteExist(id, this)) {
                //button defaults to add.
                //if it exists in the table, it should be toggled
                toggleFavoritesButton();

                //button defaults to disabled until data loads
                favorites.setEnabled(true);

                //checks for reviews in the favorites reviews
                if (DBApi.favoriteReviewExist(id, this)) enableReviews();

                //loads backdrop from the database
                DBApi.getBackdrop(id, backdrop, this);

                //button is disabled if the poster is not available
                if (!DBApi.hasPoster(id, this)) disablePoster();
            } else {
                //get reviews from network
                new getReviewsFromNetwork().execute();

                //set backdrop image from network
                if (!MovieDataObject.equalsBaseURL(movie.getBackdrop())) {
                    Picasso.with(this)
                            .load(movie.getBackdrop()).placeholder(R.drawable.blank)
                            .into(backdrop);
                }

                //button is disabled if the poster is not available
                if (MovieDataObject.equalsBaseURL(movie.getPoster()))
                    disablePoster();
            }

            //direct sets
            title.setText(movie.getMovie_title());
            original_title.setText(movie.getOriginal_title());
            overview.setText(movie.getOverview());

            //sets with prefixes
            set_string = PublicStrings.release_date_prefix +
                    movie.getRelease_date();
            release_date.setText(set_string);

            set_string = PublicStrings.popularity_prefix +
                    Double.toString(movie.getPopularity());
            popularity.setText(set_string);

            set_string = PublicStrings.vote_average_prefix +
                    Double.toString(movie.getVote_average());
            vote_average.setText(set_string);
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

        //click listener for favorites
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavoritesDB();
                toggleFavoritesButton();
            }
        });

        //click listener for trailer
        trailers.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view,
                                       int position,
                                       long id) {
                //first entry is always a label, prevents launching
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

    //toggles text of the favorites button between adding and removing favorites
    private void toggleFavoritesButton() {
        switch (favorites.getText().toString()) {
            case PublicStrings.add_favorite:
                favorites.setText(PublicStrings.remove_favorite);
                break;
            case PublicStrings.remove_favorite:
                favorites.setText(PublicStrings.add_favorite);
                break;
        }
    }

    //adds or removes from the favorites table based on the current button text
    private void toggleFavoritesDB() {
        switch (favorites.getText().toString()) {
            case PublicStrings.add_favorite:
                DBApi.addFavorite(movie, this);
                break;
            case PublicStrings.remove_favorite:
                DBApi.removeFavorite(movie.getId(), this);
                //disables reviews and adding to favorites
                //then loads reviews from network
                favorites.setEnabled(false);
                reviews.setEnabled(false);
                new getReviewsFromNetwork().execute();
                break;
        }
    }

    //starts activity to view the poster
    private void posterClicked() {
        Context context = this;
        Class destination = ViewPoster.class;
        Intent intent = new Intent(context, destination);
        if (favorites.getText().toString().equals(PublicStrings.add_favorite))
            intent.putExtra(PublicStrings.poster_intent_item_url, movie.getPoster());
        else intent.putExtra(PublicStrings.poster_intent_item_id, movie.getId());
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
        Class destination = ReviewsActivity.class;
        Intent intent = new Intent(this, destination);
        //launches intent with id if it's in the database
        //(checked using favorites button text)
        if (favorites.getText().toString().equals(PublicStrings.remove_favorite))
            intent.putExtra(PublicStrings.reviews_intent_item, movie.getId());
        startActivity(intent);
    }

    //launches intent for trailer
    private void trailerClicked(int index) {
        //resets the selection to the spinner label before launching the intent
        trailers.setSelection(0);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format
                (PublicStrings.base_youtube_url, youtube_keys[index]))));
    }

    //gets the list of trailers from the network
    private class getTrailers extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            return MovieJSONUtils.getData(MovieJSONUtils.buildURL(movie.getId(),
                    "videos"));
        }

        @Override
        protected void onPostExecute(String s) {
            parseTrailers(s);
            trailer_adapter = new ArrayAdapter<>(getBaseContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    spinner_items);
            trailers.setAdapter(trailer_adapter);
        }

        //creates string array for the adapter to use to populate the spinner
        void parseTrailers(String json_results) {
            //prevents loading empty data
            if (json_results == null) return;
            try {
                JSONObject search_object = new JSONObject(json_results);
                JSONArray array_results = search_object.getJSONArray("results");
                //adds one to make the first item a label for the spinner
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

    //used to enable the reviews button
    private void enableReviews() {
        reviews.setText("Check Reviews");
        reviews.setEnabled(true);
    }

    //used to disable view poster button
    private void disablePoster() {
        poster.setEnabled(false);
        poster.setText(PublicStrings.poster_not_available_message);
    }

    //Get reviews from network.  This call is made to check for reviews and
    //decide if the reviews button should be enabled.  Returned data is loaded
    //in to the temp table so it can be loaded in to either favorites reviews
    //or the reviews activity.
    private class getReviewsFromNetwork extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            return MovieJSONUtils.getData(MovieJSONUtils.buildURL(movie.getId(),
                    "reviews"));
        }

        @Override
        protected void onPostExecute(String s) {
            parseReviews(s);

            //once data has been loaded, allow adding to favorites
            favorites.setEnabled(true);
        }

        void parseReviews(String json_results) {

            //prevents loading empty data
            if (json_results == null) return;

            try {
                JSONObject search_object = new JSONObject(json_results);

                //prevents extra work and prevents enabling the reviews button
                if (search_object.getInt("total_results") == 0) return;
                JSONArray array_results = search_object.getJSONArray("results");
                Context c = getBaseContext();

                //clears temp table before loading
                DBApi.deleteTemp(c);
                for (int counter = 0; counter < array_results.length(); counter++) {
                    JSONObject parsed_trailer = array_results.getJSONObject(counter);

                    //adds to temp table
                    DBApi.addTempReview(parsed_trailer.getString("author"),
                            parsed_trailer.getString("content"), c);
                }

                //enable launching reviews activity once they have been loaded
                enableReviews();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}