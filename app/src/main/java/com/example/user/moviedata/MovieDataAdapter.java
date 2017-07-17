package com.example.user.moviedata;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by User on 7/10/2017.
 */

public class MovieDataAdapter extends RecyclerView.Adapter<MovieDataAdapter.PosterCell> {

    static Resources res;
    static final String base_image_url = "https://image.tmdb.org/t/p/w500";

    private MovieDataObject[] search_results;

    public MovieDataAdapter() {
        search_results = null;
    }

    @Override
    public PosterCell onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.poster_grid;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attach_immediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, attach_immediately);
        return new PosterCell(view);
    }

    @Override
    public void onBindViewHolder(PosterCell holder, int position) {
        holder.bind(search_results[position]);
    }

    @Override
    public int getItemCount() {
        if (search_results == null) return 0;
        else return search_results.length;
    }

    public void setData(MovieDataObject[] passed_results) {
        search_results = passed_results;
        notifyDataSetChanged();
    }

    class PosterCell extends RecyclerView.ViewHolder {
        TextView poster_text;
        ImageView poster_picture;


        public PosterCell(View itemView) {
            super(itemView);
            poster_text = (TextView) itemView.findViewById(R.id.cell_text);
            poster_picture = (ImageView) itemView.findViewById(R.id.cell_poster);
        }

        void bind(MovieDataObject movie) {
            //poster_picture.setImageURI(movie.poster);
            new getPoster().execute(movie.getPoster());
            poster_text.setText(movie.getMovie_title());
        }

        class getPoster extends AsyncTask<String, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(String... params) {
                URL resource_url = buildURL(params[0]);
                HttpURLConnection connection;
                InputStream stream;
                Bitmap poster;

                try {
                    connection = (HttpURLConnection) resource_url.openConnection();
                    stream = connection.getInputStream();

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

                poster = BitmapFactory.decodeStream(stream);

                return poster;
            }

            @Override
            protected void onPostExecute(Bitmap returned_data) {
                poster_picture.setImageBitmap(returned_data);
            }

            private URL buildURL(String resource_path) {
                try {
                    return new URL(base_image_url + resource_path);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }
}