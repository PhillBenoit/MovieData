package com.example.user.moviedata;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements MovieDataAdapter.MovieDataAdapterOnClickHandler{

    private RecyclerView recycler;
    private MovieDataAdapter search_grid;

    private String last_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign objects from XML layout
        recycler = (RecyclerView) findViewById(R.id.results);
        search_grid = new MovieDataAdapter(this);

        //init recycler
        GridLayoutManager layout_manager = new GridLayoutManager(this, 3);
        recycler.setLayoutManager(layout_manager);
        recycler.setHasFixedSize(true);
        recycler.setAdapter(search_grid);

        //manually runs a top rated search
        last_selected = "Top Rated";
        runSearch("top_rated");
    }

    //creates the menu from XML
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //click handler for menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String clicked = item.getTitle().toString();

        //keeps it from repeating the search already in the window
        if (!clicked.equals(last_selected)) {

            //requests new search results
            switch (clicked) {
                case "Top Rated":
                    last_selected = "Top Rated";
                    runSearch("top_rated");
                    return true;

                case "Most Popular":
                    last_selected = "Most Popular";
                    runSearch("popular");
                    return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //runs search based on given search type
    private void runSearch(String search_type) {
        new getMovies().execute(PublicStrings.base_search_url,
                search_type, PrivateStrings.api_key);
    }

    //click handler for search results
    //starts Details activity by attaching movie object to intent using a parcelable
    @Override
    public void onClick(MovieDataObject movie) {
        Context context = MainActivity.this;
        Class destination = MovieDetails.class;
        Intent intent = new Intent(context, destination);
        intent.putExtra(PublicStrings.details_intent_item, (Parcelable) movie);
        startActivity(intent);
    }

    //class for the background task to populate search results
    public class getMovies extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //builds search url and executes search
        @Override
        protected String doInBackground(String... params) {
            URL search_url = MovieJSONUtils.buildURL(params[0], params[1], params[2]);
            return MovieJSONUtils.getData(search_url);
        }

        //sets data to search results
        @Override
        protected void onPostExecute(String returned_data) {
            search_grid.setData(MovieJSONUtils.parseJSON(returned_data));
        }
    }
}