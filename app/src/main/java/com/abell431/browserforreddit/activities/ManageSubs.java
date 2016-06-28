package com.abell431.browserforreddit.activities;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.abell431.browserforreddit.R;
import com.abell431.browserforreddit.RecyclerViewAdapter;
import com.abell431.browserforreddit.helper.OnStartDragListener;
import com.abell431.browserforreddit.helper.SimpleItemTouchHelperCallback;
import com.abell431.browserforreddit.utils;

public class ManageSubs extends AppCompatActivity implements OnStartDragListener {
    RecyclerView subredditList;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private ItemTouchHelper mItemTouchHelper;
    final Context c = this;
    SharedPreferences prefs;
    final String SUBBREDDIT_PREF_KEY="SUBREDDIT_PREF_KEY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_subs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Manage Subreddits");
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prefs = this.getSharedPreferences(
                "com.abell431.browserforreddit", Context.MODE_PRIVATE);
        subredditList=(RecyclerView)findViewById(R.id.subList);
        linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerViewAdapter = new RecyclerViewAdapter(getBaseContext(),this);
        subredditList.setAdapter(mRecyclerViewAdapter);
        subredditList.setLayoutManager(linearLayoutManager);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mRecyclerViewAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(subredditList);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        String m_Text;


        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void openDialog(){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
        View mView = layoutInflaterAndroid.inflate(R.layout.input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        mRecyclerViewAdapter.add(mRecyclerViewAdapter.getItemCount(),userInputDialogEditText.getText().toString());
                       // mRecyclerViewAdapter.notifyDataSetChanged();
                        String subs= utils.arrayToString(mRecyclerViewAdapter.getmItems());
                        prefs.edit().putString(SUBBREDDIT_PREF_KEY,subs).apply();

                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
