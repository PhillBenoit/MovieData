package com.example.user.moviedata.Details;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.user.moviedata.ContentProvider.DBApi;
import com.example.user.moviedata.ContentProvider.DBContract;
import com.example.user.moviedata.PublicStrings;
import com.example.user.moviedata.R;

public class ReviewsActivity extends AppCompatActivity {

    private ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.reviews);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        adapter = new ReviewAdapter();
        recycler.setLayoutManager(layout);
        recycler.setAdapter(adapter);

        //forking logic to load review data from either the favorites reviews
        //with an id passed with the intent or temp table with the default 0 id
        Intent intent = getIntent();
        int i = 0;
        if (intent.hasExtra(PublicStrings.reviews_intent_item))
            i = intent.getIntExtra(PublicStrings.reviews_intent_item, 0);
        runCursor(DBApi.getReviewsCursor(i, this));

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

    //Runs cursor created from forked logic and loads data in to the adapter
    private void runCursor(Cursor c) {
        int result_count = c.getCount();
        if (result_count == 0) return;
        int author_column_index, review_column_index, counter = 0;
        author_column_index = c.getColumnIndex(DBContract.TempReviews.COLUMN_AUTHOR);
        review_column_index = c.getColumnIndex(DBContract.TempReviews.COLUMN_REVIEW);
        String[] authors = new String[result_count];
        String[] reviews = new String[result_count];
        c.moveToFirst();
        do {
            authors[counter] = c.getString(author_column_index);
            reviews[counter] = c.getString(review_column_index);
            counter++;
        } while (c.moveToNext());
        adapter.setData(authors, reviews);
        c.close();
    }
}
