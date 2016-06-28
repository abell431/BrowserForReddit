package com.abell431.browserforreddit.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.abell431.browserforreddit.AnalyticsApplication;
import com.abell431.browserforreddit.data.FavContract;
import com.abell431.browserforreddit.ListItems;
import com.abell431.browserforreddit.R;
import com.abell431.browserforreddit.fragments.RedditItemDetailFragment;
import com.abell431.browserforreddit.RedditRecyclerAdapter;
import com.abell431.browserforreddit.utils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of RedditItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RedditItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RedditItemListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    Parcelable mListState;
    private static final String TAG = "RecyclerViewExample";
    private int counter = 0;
    private String count;
    String LISTSTATE_KEY="listState";
    String LISTSITEMS="listItems";
    private String after_id;
    private static final String redditUrl = "http://www.reddit.com/";
    private static final String subredditUrl = "r/";
    private static final String jsonEnd = "/.json";
    private static final String qCount = "?count=";
    private static final String after = "&after=";
    private RecyclerView mRecyclerView;
    private RedditRecyclerAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private boolean mTwoPane;
    private ActionBarDrawerToggle mDrawerToggle;
    Menu drawerMenu;
    final String SUBBREDDIT_PREF_KEY="SUBREDDIT_PREF_KEY";
    SharedPreferences prefs;
    Toolbar toolbar;
    private Tracker mTracker;
    private NavigationView navigationView;
    private ArrayList<ListItems> listItemsList = new ArrayList<ListItems>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddititem_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerMenu = navigationView.getMenu();
        prefs = this.getSharedPreferences(
                "com.abell431.browserforreddit", Context.MODE_PRIVATE);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.openDes,  /* "open drawer" description */
                R.string.closedDes  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
               // getActionBar().setTitle(mDrawerTitle);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // Set the drawer toggle as the DrawerListener
       //Set the subreddits up in the navigation menu
       setSubrreddits();

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getGroupId()==R.id.group1){
                    Intent openSetting=new Intent(getBaseContext(),ManageSubs.class);
                    openSetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(openSetting);
                    mDrawerLayout.closeDrawers();
                    return true;
                }
                if (menuItem.getGroupId()==R.id.groupFav){
                   initLoader();
                    toolbar.setTitle("Favourites");
                    mDrawerLayout.closeDrawers();

                    return true;
                }
                else updateList(menuItem.toString());
                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                return true;
            }
        });


         mRecyclerView = (RecyclerView)findViewById(R.id.reddititem_list);
        assert mRecyclerView != null;

        //Check for savedInstanceState
        if(savedInstanceState==null){
            updateList(getResources().getString(R.string.Frontpage));
            getSupportActionBar().setTitle(R.string.Frontpage);
        }

        else{
            listItemsList=savedInstanceState.getParcelableArrayList(LISTSITEMS);
            mListState=savedInstanceState.getParcelable(LISTSTATE_KEY);
            adapter = new RedditRecyclerAdapter(this, listItemsList);
            mRecyclerView.setAdapter(adapter);
            adapter.SetOnItemClickListener(adapterClick);
        }



        if (findViewById(R.id.reddititem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
            //getSupportFragmentManager().beginTransaction().replace(R.id.reddititem_detail_container,new RedditItemDetailFragment()).commit();
        }
        else
        {
            Log.i("twopane","false");
            mTwoPane=false;
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        // Save list state
        Log.i("onSaveInstance","Save outstate");
        outState.putParcelable(LISTSTATE_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(LISTSITEMS,listItemsList);

    }

public void setSubrreddits(){
    MenuItem saveThis=drawerMenu.getItem(0);
    //int groupIDtoRemove=drawerMenu.getItem(1).getGroupId();
    drawerMenu.removeGroup(500);
    if (prefs.getBoolean( getString(R.string.first_run),true)) {
        // Do first run stuff here then set 'firstrun' as false
        // using the following line to edit/commit prefs
        prefs.edit().putString(SUBBREDDIT_PREF_KEY,getString(R.string.initial_subs)).commit();
        prefs.edit().putBoolean(getString(R.string.first_run), false).commit();}




    String subString=prefs.getString(SUBBREDDIT_PREF_KEY,"");
    ArrayList<String> mItems= utils.stringToArray(subString);
    for (int i = 0; i <mItems.size() ; i++) {
        drawerMenu.add(500,Menu.NONE,Menu.NONE,mItems.get(i));
    }
}
    public void updateList(String subreddit) {


        counter = 0;
        toolbar.setTitle(subreddit);

        if (subreddit.equals(getResources().getString(R.string.Frontpage))){
            subreddit = redditUrl + jsonEnd;
        }
        else{
        subreddit = redditUrl + subredditUrl + subreddit + jsonEnd;}


        adapter = new RedditRecyclerAdapter(this, listItemsList);
        mRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(adapterClick);

        RequestQueue queue = Volley.newRequestQueue(this);

        adapter.clearAdapter();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, subreddit,(String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                // Parse json data.
                try {
                    JSONObject data = response.getJSONObject("data");
                    after_id = data.getString("after");
                    JSONArray children = data.getJSONArray("children");

                    for (int i = 0; i < children.length(); i++) {

                        JSONObject post = children.getJSONObject(i).getJSONObject("data");
                        ListItems item = new ListItems();
                        item.setTitle(post.getString("title"));
                        item.setThumbnail(post.getString("thumbnail"));
                        item.setUrl(post.getString("url"));
                        item.setSubreddit(post.getString("subreddit"));
                        item.setAuthor(post.getString("author"));
                        item.setNumComments(post.getInt("num_comments"));
                        item.setScore(post.getInt("score"));
                        item.setOver18(post.getBoolean("over_18"));
                        item.setPermalink(post.getString("permalink"));
                        item.setPostedOn(post.getLong("created_utc"));
                        item.setId(post.getString("id"));
                        try {
                            Log.i("image",post.getJSONObject("preview").getJSONArray("images").getJSONObject(0).getJSONObject("source").getString("url"));
                            item.setImageUrl(post.getJSONObject("preview").getJSONArray("images").getJSONObject(0).getJSONObject("source").getString("url"));
                        }catch (JSONException e){

                        }

                        listItemsList.add(item);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update list by notifying the adapter of changes
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        queue.add(jsObjRequest);

    }

    public RedditRecyclerAdapter.OnItemClickListener adapterClick =new RedditRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            ListItems item=adapter.getListItems().get(position);
            item.getTitle();

            if (mTwoPane) {

                Bundle arguments = new Bundle();
                arguments.putString("title",item.getTitle());
                arguments.putString("subreddit",item.getSubreddit());
                arguments.putString("image_url",item.getImageUrl());
                arguments.putString("url",item.getUrl());
                arguments.putInt("score",item.getScore());
                arguments.putString("thumbnail",item.getThumbnail());
                arguments.putLong("postedOn",item.getPostedOn());
                arguments.putInt("num_comments",item.getNumComments());
                arguments.putString("permalink",item.getPermalink());
                arguments.putString("id",item.getId());
                arguments.putString("author",item.getAuthor());

                RedditItemDetailFragment fragment = new RedditItemDetailFragment();

                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.reddititem_detail_container, fragment)
                        .commit();
            }
            else {
                Intent openDetailActivity=new Intent(getBaseContext(),RedditItemDetailActivity.class);

                openDetailActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle arguments = new Bundle();
                arguments.putString("title",item.getTitle());
                arguments.putString("subreddit",item.getSubreddit());
                arguments.putString("image_url",item.getImageUrl());
                arguments.putString("url",item.getUrl());
                arguments.putInt("score",item.getScore());
                arguments.putString("thumbnail",item.getThumbnail());
                arguments.putLong("postedOn",item.getPostedOn());
                arguments.putInt("num_comments",item.getNumComments());
                arguments.putString("permalink",item.getPermalink());
                arguments.putString("id",item.getId());
                arguments.putString("author",item.getAuthor());
                openDetailActivity.putExtras(arguments);
                getBaseContext().startActivity(openDetailActivity);}

        }


    };

private void initLoader(){
    getLoaderManager().initLoader(0,null,this);
}

    public void updateViewWithResults(List<ListItems> result) {
        adapter = new RedditRecyclerAdapter(this, result);
        mRecyclerView.setAdapter(adapter);

    }
    @Override
    public void onResume(){
        super.onResume();
        setSubrreddits();
        Log.i(TAG, "Setting screen name: " + "List Activity");
        mTracker.setScreenName("Image~" + "List Activity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, FavContract.favourite.CONTENT_URI, null,
                 FavContract.favourite.COLUMN_FAVORITES + "=?", new String[]{Integer.toString(1)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        getDataFromCursor(data);
        adapter.SetOnItemClickListener(adapterClick);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    private void getDataFromCursor(Cursor cursor) {

        listItemsList.clear();
        if (cursor != null) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                ListItems item = new ListItems();
                item.setId(cursor.getString(1));
                item.setTitle(cursor.getString(2));
                item.setAuthor(cursor.getString(3));
                item.setThumbnail(cursor.getString(4));
                item.setPermalink(cursor.getString(5));
                item.setUrl(cursor.getString(6));
                item.setImageUrl(cursor.getString(7));
                item.setNumComments(cursor.getInt(8));
                item.setScore(cursor.getInt(9));
                item.setPostedOn(cursor.getLong(11));
                item.setOver18(false);
                item.setSubreddit(cursor.getString(12));

               listItemsList.add(item);


            }

                updateViewWithResults(listItemsList);}

}}
