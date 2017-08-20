package com.example.user.moviedata;

import android.os.Parcel;
import android.os.Parcelable;

// Data structure to store search results that implements parcelable so
// that movie data can be passed to the details activity
public class MovieDataObject implements Parcelable{

    private String movie_title, original_title, overview, release_date;
    private String poster, backdrop;
    private double popularity, vote_average;
    private int id;

    //determines if a string is equal to a null result
    //used to prevent pictures from loading from bad URLs
    public static boolean equalsBaseURL(String testString) {
        return testString.equals(PublicStrings.base_image_url +
                PublicStrings.null_string);
    }

    public MovieDataObject() {
        movie_title = null;
        original_title = null;
        overview = null;
        release_date = null;

        poster = null;
        backdrop = null;

        popularity = 0;
        vote_average = 0;

        id = 0;
    }

    //unpacks parcel
    private MovieDataObject(Parcel in) {
        movie_title = in.readString();
        original_title = in.readString();
        overview = in.readString();
        release_date = in.readString();

        poster = in.readString();
        backdrop = in.readString();

        popularity = in.readDouble();
        vote_average = in.readDouble();

        id = in.readInt();
    }

    //declares methods for deconstructing parcels
    public static final Creator<MovieDataObject>
            CREATOR = new Creator<MovieDataObject>() {
        @Override
        public MovieDataObject createFromParcel(Parcel in) {
            return new MovieDataObject(in);
        }

        @Override
        public MovieDataObject[] newArray(int size) {
            return new MovieDataObject[size];
        }
    };

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    //returns with base URL prefix
    public String getPoster() {
        return PublicStrings.base_image_url + poster;
    }

    public void setPoster(String url) {
        poster = url;
    }

    //returns with base URL prefix
    public String getBackdrop() {
        return PublicStrings.base_image_url + backdrop;
    }

    public void setBackdrop(String url) {
        backdrop = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //constructs parcel from object
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movie_title);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(release_date);

        dest.writeString(poster);
        dest.writeString(backdrop);

        dest.writeDouble(popularity);
        dest.writeDouble(vote_average);

        dest.writeInt(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}