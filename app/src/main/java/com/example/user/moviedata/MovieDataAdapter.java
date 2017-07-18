package com.example.user.moviedata;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by User on 7/10/2017.
 */

public class MovieDataAdapter extends RecyclerView.Adapter<MovieDataAdapter.PosterCell> {

    private MovieDataObject[] search_results;
    private final MovieDataAdapterOnClickHandler click_handler;

    public MovieDataAdapter(MovieDataAdapterOnClickHandler c_h) {
        search_results = null;
        click_handler = c_h;
    }

    public interface MovieDataAdapterOnClickHandler {
        void onClick(MovieDataObject movie);
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

    class PosterCell extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView poster_text;
        ImageView poster_picture;

        public PosterCell(View itemView) {
            super(itemView);
            poster_text = (TextView) itemView.findViewById(R.id.cell_text);
            poster_picture = (ImageView) itemView.findViewById(R.id.cell_poster);
            itemView.setOnClickListener(this);
        }

        void bind(MovieDataObject movie) {
            Bitmap poster = movie.getPoster();
            //if (poster != null)
            poster_picture.setImageBitmap(poster);
            poster_text.setText(movie.getMovie_title());
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            click_handler.onClick(search_results[index]);
        }
    }
}