package com.example.user.moviedata.Details;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.moviedata.R;

/**
 * Created by User on 8/7/2017.
 * Helps with loading review data for the reviews activity
 */

class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.review> {

    //actual data held in the adapter
    private String[] reviews, authors;

    ReviewAdapter() {
        reviews = new String[]{""};
        authors = new String[]{""};
    }

    @Override
    public ReviewAdapter.review onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.review;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new review(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.review holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (reviews == null) return 0;
        else return reviews.length;
    }

    void setData(String[] authors, String[] reviews) {
        this.reviews = reviews;
        this.authors = authors;
        notifyDataSetChanged();
    }

    class review extends RecyclerView.ViewHolder {

        TextView author, review;

        review(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.review_author);
            review = (TextView) itemView.findViewById(R.id.review_content);
        }

        void bind(int index) {
            author.setText(authors[index]);
            review.setText(reviews[index]);
            //alternates background colors of reviews
            if (index % 2 == 1) {
                LinearLayout parent = (LinearLayout) itemView.findViewById(R.id.review_parent);
                parent.setBackgroundColor(Color.parseColor("#bbdefb"));
            }
        }
    }
}
