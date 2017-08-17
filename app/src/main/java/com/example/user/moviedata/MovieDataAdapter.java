package com.example.user.moviedata;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.moviedata.ContentProvider.DBApi;
import com.squareup.picasso.Picasso;

//connects movies to cells from search results
public class MovieDataAdapter extends RecyclerView.Adapter<MovieDataAdapter.PosterCell> {

    private MovieDataObject[] search_results;
    private final MovieDataAdapterOnClickHandler click_handler;

    public MovieDataAdapter(MovieDataAdapterOnClickHandler c_h) {
        search_results = null;
        click_handler = c_h;
    }

    //creates base interface overwritten in main activity
    public interface MovieDataAdapterOnClickHandler {
        void onClick(MovieDataObject movie);
    }

    //attaches a poster cell to the view holder for recycler view
    @Override
    public PosterCell onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.poster_grid;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean attach_immediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, attach_immediately);
        return new PosterCell(view);
    }

    //binds data to a cell
    @Override
    public void onBindViewHolder(PosterCell holder, int position) {
        holder.bind(search_results[position]);
    }

    //count of how many search results are present
    @Override
    public int getItemCount() {
        if (search_results == null) return 0;
        else return search_results.length;
    }

    //incorporates search results in to this object
    public void setData(MovieDataObject[] passed_results) {
        search_results = passed_results;
        notifyDataSetChanged();
    }

    //class for the cell in the view holder
    class PosterCell extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView poster_text;
        ImageView poster_picture;
        Context context;

        //sets up the cell for the recycler view
        public PosterCell(View itemView) {
            super(itemView);
            poster_text = (TextView) itemView.findViewById(R.id.cell_text);
            poster_picture = (ImageView) itemView.findViewById(R.id.cell_poster);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        //sets text and picture of cell
        void bind(MovieDataObject movie) {
            //load image from favorites if it exists
            if (DBApi.hasPoster(movie.getId(), context))
                DBApi.getPoster(movie.getId(), poster_picture, context);
            //if (poster != null load from network)
            else if (!MovieDataObject.equalsBaseURL(movie.getPoster()))
                Picasso.with(context)
                        .load(movie.getPoster())
                        .into(poster_picture);
            poster_text.setText(movie.getMovie_title());
        }

        //click handler that finds the Movie Data Object by the same index as the clicked cell
        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            click_handler.onClick(search_results[index]);
        }
    }
}