package com.abell431.browserforreddit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by Andrew on 24/05/2016.
 */
public class RedditRecyclerAdapter extends RecyclerView.Adapter<RedditRecyclerAdapter.ListRowViewHolder> {

    private List<ListItems> listItemsList;
    private Context mContext;
    private ImageLoader mImageLoader;
   OnItemClickListener mItemClickListener;

    private int focusedItem = 0;


    public RedditRecyclerAdapter(Context context, List<ListItems> listItemsList) {
        this.listItemsList = listItemsList;
        this.mContext = context;
    }

    @Override
    public RedditRecyclerAdapter.ListRowViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reddititem_list_content, null);
        final ListRowViewHolder holder = new ListRowViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ListRowViewHolder holder, int position) {
        ListItems listItems = listItemsList.get(position);
        holder.itemView.setSelected(focusedItem == position);



        mImageLoader = MySingleton.getInstance(mContext).getImageLoader();

        holder.thumbnail.setImageUrl(listItems.getThumbnail(), mImageLoader);
        //  listRowViewHolder.thumbnail.setDefaultImageResId(R.drawable.ic_action_menu);

        holder.title.setText(Html.fromHtml(listItems.getTitle()));
        //    listRowViewHolder.url.setText(Html.fromHtml(listItems.getUrl()));
        holder.subreddit.setText("r/"+Html.fromHtml(listItems.getSubreddit()));
        holder.comments.setText((Html.fromHtml(String.valueOf(listItems.getNumComments())))+" comments");
        holder.score.setText(Html.fromHtml(String.valueOf(listItems.getScore()))+" points");
        //      listRowViewHolder.author.setText(Html.fromHtml(listItems.getAuthor()));

    }

public List<ListItems> getListItems(){
    return  listItemsList;
}

    public void clearAdapter()
    {
        listItemsList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        Log.i("item",String.valueOf(listItemsList.size()));
        return (null != listItemsList ? listItemsList.size() : 0);
    }

    public class ListRowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected NetworkImageView thumbnail;
        protected TextView title;
        protected TextView url;
        protected RelativeLayout recLayout;
        protected TextView author;
        protected TextView subreddit, score, comments;


        public ListRowViewHolder(View view) {
            super(view);
            this.thumbnail = (NetworkImageView) view.findViewById(R.id.avatar_imageview);
            this.title = (TextView) view.findViewById(R.id.id);
            this.recLayout = (RelativeLayout) view.findViewById(R.id.relCardLayout);
            this.subreddit = (TextView) view.findViewById(R.id.content);
            this.score = (TextView) view.findViewById(R.id.score);
            this.comments = (TextView) view.findViewById(R.id.comments);
            view.setClickable(true);
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }


    }
    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}