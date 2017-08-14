package com.example.user.moviedata.Details;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.user.moviedata.PublicStrings;
import com.example.user.moviedata.R;
import com.squareup.picasso.Picasso;

public class ViewPoster extends AppCompatActivity {

    ImageView poster;

    //task to view the poster
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_poster);
        poster = (ImageView) findViewById(R.id.ViewPoster);
        Intent intent = getIntent();

        if (intent.hasExtra(PublicStrings.poster_intent_item)) {
            Picasso.with(this)
                    .load(intent.getStringExtra(PublicStrings.poster_intent_item))
                    .into(poster);
        }

        //enable back button
        android.app.ActionBar bar = getActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);
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
}
