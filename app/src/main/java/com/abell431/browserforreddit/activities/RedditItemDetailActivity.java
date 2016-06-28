package com.abell431.browserforreddit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.abell431.browserforreddit.R;
import com.abell431.browserforreddit.fragments.RedditItemDetailFragment;
import com.android.volley.toolbox.ImageLoader;

/**
 * An activity representing a single RedditItem detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RedditItemListActivity}.
 */
public class RedditItemDetailActivity extends AppCompatActivity {
    private ImageLoader mImageLoader;
    Boolean isFavourite;
    String id;

    Bundle extras;

    CollapsingToolbarLayout mCollapsableToolbar;
    public static final String ACTION_DATA_UPDATED =
            "come.abell431.browserforreddit.ACTION_DATA_UPDATED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddititem_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        extras=getIntent().getExtras();
        id=extras.getString("id");

        toolbar.setTitle(extras.getString("subreddit"));


toolbar.setTitle("AskReddit");




       //
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        //Log.i("detailIntentAct",getIntent().getExtras().getString("title"));
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(RedditItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(RedditItemDetailFragment.ARG_ITEM_ID));
            RedditItemDetailFragment fragment = new RedditItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameLayout, fragment)
                    .commit();
        }
    }

    @Override
    public void onStart(){

        super.onStart();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, RedditItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
