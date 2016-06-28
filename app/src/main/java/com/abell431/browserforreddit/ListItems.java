package com.abell431.browserforreddit;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrew on 24/05/2016.
 */
public class ListItems implements Parcelable {
    private String title;
    private String thumbnail;
    private String url;
    private String subreddit;
    private String author;
    private String permalink;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private long postedOn;
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;

    public long getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(long postedOn) {
        this.postedOn = postedOn;
    }

    private int score,numComments;
    private Boolean over18;

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getSubreddit() { return subreddit; }

    public void setSubreddit(String subreddit) { this.subreddit = subreddit; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public int getScore() {return score;}

    public void setScore(int score){ this.score=score;}

    public int getNumComments() {return numComments;}

    public void setNumComments(int numComments){ this.numComments=numComments;}

    public Boolean getOver18(){return over18;}

    public void setOver18(Boolean over18){this.over18=over18;}

    public String getPermalink(){
        return permalink;
    }
    public void setPermalink(String permalink){this.permalink=permalink;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.thumbnail);
        dest.writeString(this.url);
        dest.writeString(this.subreddit);
        dest.writeString(this.author);
        dest.writeString(this.permalink);
        dest.writeString(this.id);
        dest.writeLong(this.postedOn);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.score);
        dest.writeInt(this.numComments);
        dest.writeValue(this.over18);
    }

    public ListItems() {
    }

    protected ListItems(Parcel in) {
        this.title = in.readString();
        this.thumbnail = in.readString();
        this.url = in.readString();
        this.subreddit = in.readString();
        this.author = in.readString();
        this.permalink = in.readString();
        this.id = in.readString();
        this.postedOn = in.readLong();
        this.imageUrl = in.readString();
        this.score = in.readInt();
        this.numComments = in.readInt();
        this.over18 = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ListItems> CREATOR = new Parcelable.Creator<ListItems>() {
        @Override
        public ListItems createFromParcel(Parcel source) {
            return new ListItems(source);
        }

        @Override
        public ListItems[] newArray(int size) {
            return new ListItems[size];
        }
    };
}