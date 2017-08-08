package com.example.user.moviedata;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReviewsActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        Intent intent = getIntent();
        if (intent.hasExtra(PublicStrings.reviews_intent_item)) {
            Integer i = intent.getIntExtra(PublicStrings.reviews_intent_item, 0);
            new getReviews().execute(i);
        }

        recycler = (RecyclerView) findViewById(R.id.reviews);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        adapter = new ReviewAdapter();
        recycler.setLayoutManager(layout);
        recycler.setAdapter(adapter);

        //enable back button
        android.app.ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);
    }

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

    public class getReviews extends AsyncTask<Integer, Void, String> {

        String[] authors, reviews;

        @Override
        protected String doInBackground(Integer... params) {
            return MovieJSONUtils.getData(MovieJSONUtils.buildURL(params[0], "reviews"));
        }

        @Override
        protected void onPostExecute(String s) {
            parseReviews(s);
            adapter.setData(authors, reviews);
            Log.d("TESTBUG", reviews[0]);
        }

        void parseReviews(String json_results) {
            //prevents loading empty data
            if (json_results == null) return;
            JSONObject search_object = null;
            try {
                search_object = new JSONObject(json_results);
                JSONArray array_results = search_object.getJSONArray("results");
                authors = new String[array_results.length()];
                reviews = new String[array_results.length()];
                for (int counter = 0; counter < array_results.length(); counter++) {
                    JSONObject parsed_trailer = array_results.getJSONObject(counter);
                    authors[counter] = parsed_trailer.getString("author");
                    reviews[counter] = parsed_trailer.getString("content");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
