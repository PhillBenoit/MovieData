package com.example.user.moviedata;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.user.moviedata.ContentProvider.DBApi;
import com.example.user.moviedata.Details.MovieDetails;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements MovieDataAdapter.MovieDataAdapterOnClickHandler{

    private MovieDataAdapter search_grid;
    GridLayoutManager layout_manager;

    private String last_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign objects from XML layout
        RecyclerView recycler = (RecyclerView) findViewById(R.id.results);
        search_grid = new MovieDataAdapter(this);

        //init recycler
        layout_manager = new GridLayoutManager(this, 3);
        recycler.setLayoutManager(layout_manager);
        recycler.setHasFixedSize(true);
        recycler.setAdapter(search_grid);

        //manually runs a top rated search if there is no saved state
        if (savedInstanceState == null) {
            last_selected = "Top Rated";
            runSearch("top_rated");
        }
    }

    //saves search when the screen turns
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("scroll", layout_manager.findFirstVisibleItemPosition());
        outState.putParcelableArray("movies", search_grid.getData());
    }

    //loads search after screen turn
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        search_grid.setData((MovieDataObject[])
                savedInstanceState.getParcelableArray("movies"));
        layout_manager.scrollToPosition(savedInstanceState.getInt("scroll"));
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

                case "Favorites":
                    last_selected = "Favorites";
                    loadFromFavorites();
                    return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //runs query to database and loads results to adapter
    private void loadFromFavorites(){
        search_grid.setData(DBApi.getFavorites(this));
    }

    //runs search based on given search type
    private void runSearch(String search_type) {
        new getMovies().execute(search_type);
    }

    //Click handler for search results.
    //Starts details activity by attaching movie object to
    //the intent using a parcelable.
    @Override
    public void onClick(MovieDataObject movie) {
        Context context = MainActivity.this;
        Class destination = MovieDetails.class;
        Intent intent = new Intent(context, destination);
        intent.putExtra(PublicStrings.details_intent_item, movie);
        startActivity(intent);
    }

    //class for the background task to populate search results
    private class getMovies extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //builds search url and executes search
        @Override
        protected String doInBackground(String... params) {
            URL search_url = MovieJSONUtils.buildURL(params[0]);
            return MovieJSONUtils.getData(search_url);
        }

        //sets data to search results
        @Override
        protected void onPostExecute(String returned_data) {
            search_grid.setData(MovieJSONUtils.parseJSON(returned_data));
        }
    }
}