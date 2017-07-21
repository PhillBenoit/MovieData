package com.example.user.moviedata;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

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
    }
}
