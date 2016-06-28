package com.abell431.browserforreddit.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.abell431.browserforreddit.data.FavContract;

/**
 * Created by Andrew on 30/05/2016.
 */
public class FavouriteDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME ="post.db";

    public FavouriteDBHelper(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE ="CREATE TABLE "+ FavContract.favourite.TABLE_NAME+ " ("+
                FavContract.favourite._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + //0
                FavContract.favourite.COLUMN_POST_ID +" STRING NOT NULL, " +         //1
                FavContract.favourite.COLUMN_TITLE +" TEXT NOT NULL, " +            //2
                FavContract.favourite.COLUMN_AUTHOR +" TEXT, " +                    //3
                FavContract.favourite.COLUMN_THUMBNAIL +" TEXT, " +                 //4
                FavContract.favourite.COLUMN_PERMALINK +" TEXT, " +                 //5
                FavContract.favourite.COLUMN_URL +" TEXT, " +                       //6
                FavContract.favourite.COLUMN_IMAGE_URL +" TEXT, " +                 //7
                FavContract.favourite.COLUMN_COMMENTS +" INTEGER, " +               //8
                FavContract.favourite.COLUMN_POINTS +" INTEGER, " +                 //9
                FavContract.favourite.COLUMN_FAVORITES +" INTEGER NOT NULL," +      //10
                FavContract.favourite.COLUMN_POSTED_ON +" LONG,"+      //11
                FavContract.favourite.COLUMN_SUBREDDIT +" STRING);" ;                //12

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " +FavContract.favourite.TABLE_NAME);
        onCreate(db);
    }
}