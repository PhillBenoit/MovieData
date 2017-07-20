package com.example.user.moviedata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;


/**
 * Created by User on 7/11/2017.
 */

public class MovieDataObject implements Parcelable{

    private String movie_title, original_title, overview, release_date;
    private Bitmap poster, backdrop;
    private double popularity, vote_average;

    public MovieDataObject() {
        movie_title = "";
        original_title = "";
        overview = "";
        release_date = "";

        poster = null;
        backdrop = null;

        popularity = 0;
        vote_average = 0;
    }

    protected MovieDataObject(Parcel in) {
        movie_title = in.readString();
        original_title = in.readString();
        overview = in.readString();
        release_date = in.readString();

        //poster = in.readParcelable(Bitmap.class.getClassLoader());
        //backdrop = in.readParcelable(Bitmap.class.getClassLoader());
        poster = readBMP(in.createByteArray());
        backdrop = readBMP(in.createByteArray());

        popularity = in.readDouble();
        vote_average = in.readDouble();
    }

    public static final Creator<MovieDataObject> CREATOR = new Creator<MovieDataObject>() {
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

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }

    public Bitmap getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(Bitmap backdrop) {
        this.backdrop = backdrop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movie_title);
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(release_date);

        //dest.writeParcelable(poster, flags);
        //dest.writeParcelable(backdrop, flags);
        dest.writeByteArray(writeBMP(poster));
        dest.writeByteArray(writeBMP(backdrop));

        dest.writeDouble(popularity);
        dest.writeDouble(vote_average);
    }

    private byte[] writeBMP(Bitmap picture) {
        if (picture != null) {
            ByteArrayOutputStream byte_stream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.PNG, 100, byte_stream);
            return byte_stream.toByteArray();
        } else {
            byte[] temp = {Byte.parseByte("0"), Byte.parseByte("0")};
            return temp;
        }
    }

    private Bitmap readBMP(byte[] png) {
        if (png.length != 2) {
            return BitmapFactory.decodeByteArray(png, 0, png.length);
        } else {
            return null;
        }
    }
}