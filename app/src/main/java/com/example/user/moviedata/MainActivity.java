package com.example.user.moviedata;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements MovieDataAdapter.MovieDataAdapterOnClickHandler{

    Resources res;

    private EditText search_text;
    private TextView search_hint;
    private RecyclerView recycler;
    private MovieDataAdapter search_grid;
    private String search_url;
    private static final String APIkey = "f1a61c945d3b5771b361206661d63bd5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_text = (EditText) findViewById(R.id.search_box);
        search_hint = (TextView) findViewById(R.id.search_hint);
        recycler = (RecyclerView) findViewById(R.id.results);
        search_grid = new MovieDataAdapter(this);
        res = getResources();
        search_url = res.getString(R.string.query_url);

        GridLayoutManager layout_manager = new GridLayoutManager(this, 3);
        recycler.setLayoutManager(layout_manager);
        recycler.setHasFixedSize(true);

        recycler.setAdapter(search_grid);

        //run search
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            runSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void runSearch() {
        String search = search_text.getText().toString();
        if (!search.isEmpty()) {
            search_hint.setVisibility(View.INVISIBLE);
            recycler.setVisibility(View.VISIBLE);

            new getMovies().execute(search_url, APIkey, search);
        }
    }

    @Override
    public void onClick(MovieDataObject movie) {
        //Log.d("TESTBUG", movie.getMovie_title());
        Context context = MainActivity.this;
        Class destination = MovieDetails.class;
        Intent intent = new Intent(context, destination);
        intent.putExtra("MOVIE_PARCEL", (Parcelable) movie);
        startActivity(intent);
    }

    public class getMovies extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(String... params) {
            URL search_url = MovieJSONAdapter.buildURL(params[0], params[1], params[2]);
            String[] data_return = new String[1];

            data_return[0] = MovieJSONAdapter.getData(search_url);

            return data_return;
        }

        @Override
        protected void onPostExecute(String[] returned_data) {
            MovieJSONAdapter parsed_results = new MovieJSONAdapter(returned_data[0]);
            search_grid.setData(parsed_results.getResults());
        }
    }
}
