package com.abell431.browserforreddit.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.abell431.browserforreddit.ListItems;
import com.abell431.browserforreddit.R;
import com.abell431.browserforreddit.data.FavContract;

/**
 * Created by Andrew on 30/05/2016.
 */
public class FavouriteWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(FavContract.favourite.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();

            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.favourite_widget_list_item);

                String title=data.getString(2);
                String subreddit=data.getString(12);
                int points=data.getInt(9);
                int comments=data.getInt(8);

                views.setTextViewText(R.id.widgetTitle, title);
                views.setTextViewText(R.id.widgeSub, "r/"+subreddit);
                views.setTextViewText(R.id.widgetPoints, String.valueOf(points)+" Points");
                views.setTextViewText(R.id.widgetComments, String.valueOf(comments+" Comments"));
                //final Intent fillInIntent = new Intent();
                Intent openDetailActivity = new Intent();



                ListItems item = new ListItems();
                item.setId(data.getString(1));
                item.setTitle(data.getString(2));
                item.setAuthor(data.getString(3));
                item.setThumbnail(data.getString(4));
                item.setPermalink(data.getString(5));
                item.setUrl(data.getString(6));
                item.setImageUrl(data.getString(7));
                item.setNumComments(data.getInt(8));
                item.setScore(data.getInt(9));
                item.setPostedOn(data.getLong(11));
                item.setOver18(false);
                item.setSubreddit(data.getString(12));


                openDetailActivity.putExtra("title",item.getTitle());
                openDetailActivity.putExtra("subreddit",item.getSubreddit());
                openDetailActivity.putExtra("image_url",item.getImageUrl());
                openDetailActivity.putExtra("url",item.getUrl());
                openDetailActivity.putExtra("score",item.getScore());
                openDetailActivity.putExtra("thumbnail",item.getThumbnail());
                openDetailActivity.putExtra("postedOn",item.getPostedOn());
                openDetailActivity.putExtra("num_comments",item.getNumComments());
                openDetailActivity.putExtra("permalink",item.getPermalink());
                openDetailActivity.putExtra("id",item.getId());
                openDetailActivity.putExtra("author",item.getAuthor());
                openDetailActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                views.setOnClickFillInIntent(R.id.widget_list_item, openDetailActivity);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.favourite_widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

    ;
}
