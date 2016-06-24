package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by Semmiverian on 6/21/16.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data;

            @Override
            public void onCreate() {
                final long identityToken = Binder.clearCallingIdentity();
                data = getQuery();
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDataSetChanged() {
                final long identityToken = Binder.clearCallingIdentity();
                data = getQuery();
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
            public RemoteViews getViewAt(int i) {
                data.moveToPosition(i);
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_list_item_quote);
                views.setTextViewText(R.id.stock_symbol,data.getString(data.getColumnIndex("symbol")));
                views.setTextViewText(R.id.bid_price,data.getString(data.getColumnIndex("bid_price")));
//
                Intent graphIntent = new Intent();
                graphIntent.putExtra("symbol",data.getString(data.getColumnIndex("symbol")));
                views.setOnClickFillInIntent(R.id.stock_info,graphIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

    private Cursor getQuery() {
        return getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                        QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                QuoteColumns.ISCURRENT + " = ?",
                new String[]{"1"},
                null);

    }
}
