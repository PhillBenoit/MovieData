package com.example.user.moviedata;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by User on 8/7/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.review> {

    private String[] reviews, authors;

    public ReviewAdapter() {
        reviews = new String[]{""};
        authors = new String[]{""};
    }

    @Override
    public ReviewAdapter.review onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.poster_grid;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        boolean attach_immediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, attach_immediately);
        return new review(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.review holder, int position) {
        Log.d("TESTBUG", Integer.toString(position));
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (reviews[0].equals("")) return 0;
        else return reviews.length;
    }

    public void setData(String[] authors, String[] reviews) {
        this.reviews = reviews;
        this.authors = authors;
        notifyDataSetChanged();
    }

    class review extends RecyclerView.ViewHolder {

        TextView author, review;

        public review(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.review_author);
            review = (TextView) itemView.findViewById(R.id.review_content);
        }

        void bind(int index) {
            if (author != null && review != null) {
                author.setText(authors[index]);
                review.setText(reviews[index]);
            }
        }
    }
}
