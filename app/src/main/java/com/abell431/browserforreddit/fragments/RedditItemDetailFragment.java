package com.abell431.browserforreddit.fragments;

import android.app.Activity;

import android.content.ContentValues;
import android.content.Context;

import android.content.Intent;

import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.abell431.browserforreddit.AnalyticsApplication;
import com.abell431.browserforreddit.Comment;
import com.abell431.browserforreddit.CommentProcessor;
import com.abell431.browserforreddit.data.FavContract;
import com.abell431.browserforreddit.MySingleton;
import com.abell431.browserforreddit.R;
import com.abell431.browserforreddit.activities.RedditItemDetailActivity;
import com.abell431.browserforreddit.activities.RedditItemListActivity;
import com.abell431.browserforreddit.CommentAdapter;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

/**
 * A fragment representing a single RedditItem detail screen.
 * This fragment is either contained in a {@link RedditItemListActivity}
 * in two-pane mode (on tablets) or a {@link RedditItemDetailActivity}
 * on handsets.
 */
public class RedditItemDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    Boolean isFavourite;
    String id;
    ImageButton favStar;
    Parcelable mListState;
    Bundle extras;
    String LISTSTATE_KEY="listState";
    String LISTSITEMS="listItems";
    RecyclerView.LayoutManager mLayoutManager;
    private Tracker mTracker;
    CollapsingToolbarLayout mCollapsableToolbar;
    public static final String ACTION_DATA_UPDATED =
            "come.abell431.browserforreddit.ACTION_DATA_UPDATED";
    public static final String ARG_ITEM_ID = "item_id";
    CommentProcessor processor;
    ArrayList<Comment> comments;
    ImageLoader mImageLoader;
    RecyclerView mRecyclerView;
    CommentAdapter commentAdapter;
    ImageButton menu;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RedditItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        // Save list state
        Log.i("onSaveInstance","Save outstate");
        outState.putParcelable(LISTSTATE_KEY, mLayoutManager.onSaveInstanceState());
        outState.putParcelableArrayList(LISTSITEMS,comments);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.reddititem_detail, container, false);

        final Activity activity = this.getActivity();
        favStar=(ImageButton)rootView.findViewById(R.id.addFav);
       // comments=new ArrayList<Comment>();
        menu=(ImageButton)rootView.findViewById(R.id.menu);
        //Log.i("frag",url);
        comments=new ArrayList<Comment>();

        extras=getActivity().getIntent().getExtras();

        if (extras==null){
            extras=getArguments();
        }

        final String url="http://www.reddit.com"+extras.getString("permalink")+".json";
        Log.i("frag",url);

        processor=new CommentProcessor(url);
        id=extras.getString("id");
        class AddComments extends AsyncTask<String, Void, String> {

            protected String doInBackground(String... arg0) {
                //Your implementation
                comments.addAll(processor.fetchComments());
                return "done";
            }

            protected void onPostExecute(String result) {
                // TODO: do something with the feed
                mRecyclerView=(RecyclerView)rootView.findViewById(R.id.commentRecycler);

                commentAdapter = new CommentAdapter(activity,comments);
                mRecyclerView.setAdapter(commentAdapter);
              mLayoutManager = new LinearLayoutManager(getContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setNestedScrollingEnabled(false); // Disables scrolling



            }
        }
        if(savedInstanceState==null){
            AddComments addComments=new AddComments();
            addComments.execute(url);
        }else {
           // AddComments addComments=new AddComments();
            //addComments.execute(url);
            comments=savedInstanceState.getParcelableArrayList(LISTSITEMS);
            mListState = savedInstanceState.getParcelable(LISTSTATE_KEY);
            mRecyclerView=(RecyclerView)rootView.findViewById(R.id.commentRecycler);
            commentAdapter = new CommentAdapter(activity,comments);
            mRecyclerView.setAdapter(commentAdapter);
            mLayoutManager = new LinearLayoutManager(getContext());
           mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
            mRecyclerView.setNestedScrollingEnabled(false);
        }

       // Log.i("detailFrag","commentsprocessed"+comments.size());

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.detail_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.open:
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(extras.getString("url")));
                                startActivity(i);
                                break;
                            case R.id.share:
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, extras.getString("url"));
                                sendIntent.setType("text/plain");
                                startActivity(Intent.createChooser(sendIntent, "Share link"));
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });

        //Load Banner Ad
        //Already Init
        //MobileAds.initialize(getContext().getApplicationContext(), "ca-app-pub-0067204685712546~2086059370");
        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().
                addTestDevice(AdRequest.DEVICE_ID_EMULATOR).
                addTestDevice("C1D3074D21D7AE8AA1099BCD4ABAD9C6").
                build();
        mAdView.loadAd(adRequest);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();



        favStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFavourite) {

                    ContentValues values = new ContentValues();
                    values.put(FavContract.favourite.COLUMN_TITLE, extras.getString("title"));
                    values.put(FavContract.favourite.COLUMN_AUTHOR, extras.getString("author"));
                    values.put(FavContract.favourite.COLUMN_PERMALINK, extras.getString("permalink"));
                    values.put(FavContract.favourite.COLUMN_POINTS, extras.getInt("score"));
                    values.put(FavContract.favourite.COLUMN_COMMENTS, extras.getInt("num_comments"));
                    values.put(FavContract.favourite.COLUMN_IMAGE_URL, extras.getString("image_url"));
                    values.put(FavContract.favourite.COLUMN_URL, extras.getString("url"));
                    values.put(FavContract.favourite.COLUMN_THUMBNAIL, extras.getString("thumbnail"));
                    values.put(FavContract.favourite.COLUMN_POSTED_ON, extras.getLong("postedOn"));
                    values.put(FavContract.favourite.COLUMN_POST_ID,id);
                    values.put(FavContract.favourite.COLUMN_SUBREDDIT,extras.getString("subreddit"));
                    values.put(FavContract.favourite.COLUMN_FAVORITES, 1);
                    getContext().getContentResolver().insert(FavContract.favourite.CONTENT_URI, values);

                  //  Toast.makeText(getContext(), "Post added to favourites", Toast.LENGTH_SHORT).show();
                    favStar.setSelected(true);
                    isFavourite=true;

                }
                else
                {
                    //Toast.makeText(getContext(),"Post removed...",Toast.LENGTH_SHORT).show();

                    favStar.setSelected(false);
                    isFavourite=false;
                    //Delete from db
                    getContext().getContentResolver().delete(FavContract.favourite.CONTENT_URI,
                            FavContract.favourite.COLUMN_POST_ID + "=?", new String[]{String.valueOf(id)});
                }

                //Notify widget to update
                Context context = getContext();
                Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                        .setPackage(context.getPackageName());
                context.sendBroadcast(dataUpdatedIntent);
            }
        });

        mImageLoader = MySingleton.getInstance(getContext()).getImageLoader();
        NetworkImageView headerImage=(NetworkImageView)rootView.findViewById(R.id.headerImage);

        headerImage.setImageUrl(extras.getString("image_url"), mImageLoader);
        if(headerImage.getImageURL()==null){
            headerImage.setVisibility(View.GONE);
        }

        TextView commentsTxt=(TextView)rootView.findViewById(R.id.commentsNum);
        commentsTxt.setText(String.valueOf((extras.getInt("num_comments")))+application.getString(R.string.comments));

        TextView points=(TextView)rootView.findViewById(R.id.score);
        points.setText(String.valueOf((extras.getInt("score")))+application.getString(R.string.Points));

        TextView title=(TextView)rootView.findViewById(R.id.headerTitle);
        title.setText(String.valueOf((extras.getString("title"))));

        initLoader();
        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();
        Log.i("DetailTag", "Setting screen name: " + "Detail Fragment");
        mTracker.setScreenName("Image~" + "Detail Activity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


    }
private void initLoader(){

    getLoaderManager().initLoader(0,null,this);
}
    @Override
    public Loader<Cursor> onCreateLoader(int Id, Bundle args) {
        return new CursorLoader(getContext(),
                FavContract.favourite.CONTENT_URI,
                null,
                FavContract.favourite.COLUMN_POST_ID+"=?" ,
                new String[]{id},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int fav = 0;
        isFavourite=false;
        Log.i("loader","finished");
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                fav = cursor.getInt(10);
                // Log.i("fav","  "+cursor.getString(3));
            }
        }

        if (fav == 1) {
            isFavourite=true;
           favStar.setSelected(true);
        } else {
            isFavourite=false;
            favStar.setSelected(false);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

}
