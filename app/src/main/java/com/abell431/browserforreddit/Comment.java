package com.abell431.browserforreddit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew on 29/05/2016.
 */
public class Comment implements Parcelable {
    String htmlText,author,points,postedOn;


    // The 'level' field indicates how deep in the hierarchy
    // this comment is. A top-level comment has a level of 0
    // where as a reply has level 1, and reply of a reply has
    // level 2 and so on...
    int level;
    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.htmlText);
        dest.writeString(this.author);
        dest.writeString(this.points);
        dest.writeString(this.postedOn);
        dest.writeInt(this.level);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.htmlText = in.readString();
        this.author = in.readString();
        this.points = in.readString();
        this.postedOn = in.readString();
        this.level = in.readInt();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}