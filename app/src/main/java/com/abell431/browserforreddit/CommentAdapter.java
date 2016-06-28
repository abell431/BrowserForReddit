package com.abell431.browserforreddit;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Andrew on 29/05/2016.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private ArrayList<Comment> commentArrayList;
    private Context mContext;

    public CommentAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
        this.mContext = context;
    }
    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, null);
        final CommentHolder holder = new CommentHolder(v);
       // Log.i("holder","onCreaterViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
       /* ListItems listItems = listItemsList.get(position);
        holder.itemView.setSelected(focusedItem == position);



        mImageLoader = MySingleton.getInstance(mContext).getImageLoader();

        holder.thumbnail.setImageUrl(listItems.getThumbnail(), mImageLoader);
        //  listRowViewHolder.thumbnail.setDefaultImageResId(R.drawable.ic_action_menu);

        holder.title.setText(Html.fromHtml(listItems.getTitle()));*/
        Comment comment=commentArrayList.get(position);
        holder.author.setText(comment.getAuthor());
        holder.points.setText(comment.getPoints()+" Points");
        holder.body.setText(comment.getHtmlText());
        holder.postedOn.setText(comment.postedOn);
        holder.container.setPadding(comment.getLevel()*20,0,0,0);

        switch (comment.getLevel()){
            case 0: holder.levelIndicator.setBackgroundColor(Color.parseColor("#ff0000"));
                break;
            case 1: holder.levelIndicator.setBackgroundColor(Color.parseColor("#257425"));
                break;
            case 2: holder.levelIndicator.setBackgroundColor(Color.parseColor("#99ccff"));
                break;
            case 3: holder.levelIndicator.setBackgroundColor(Color.parseColor("#ea6459"));
                break;
            case 4: holder.levelIndicator.setBackgroundColor(Color.parseColor("#dd3c85"));
                break;
            case 5: holder.levelIndicator.setBackgroundColor(Color.parseColor("#808080"));
                break;

        }

      //  Log.i("comment",comment.author+"  "+comment.htmlText);
    }
    public void clearAdapter()
    {
        commentArrayList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }


    public class CommentHolder extends RecyclerView.ViewHolder {

        protected TextView author,body,postedOn,points;
        LinearLayout container;
        RelativeLayout levelIndicator;


        public CommentHolder(View view) {
            super(view);
            /*this.thumbnail = (NetworkImageView) view.findViewById(R.id.avatar_imageview);
            this.title = (TextView) view.findViewById(R.id.id);
            //this.url = (TextView) view.findViewById(R.id.url);
            this.recLayout = (RelativeLayout) view.findViewById(R.id.relCardLayout);
            this.subreddit = (TextView) view.findViewById(R.id.content);
            this.score = (TextView) view.findViewById(R.id.score);
            this.comments = (TextView) view.findViewById(R.id.comments); */
            // this.author = (TextView) view.findViewById(R.id.author);

            this.author=(TextView)view.findViewById(R.id.author);
            this.body=(TextView)view.findViewById(R.id.body);
            this.postedOn=(TextView)view.findViewById(R.id.postedOn);
            this.points=(TextView)view.findViewById(R.id.points);
            this.container=(LinearLayout) view.findViewById(R.id.card_linear);
            this.levelIndicator=(RelativeLayout)view.findViewById(R.id.view);

        }


    }


}
