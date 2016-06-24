package com.sam_chordas.android.stockhawk.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.StockGraphActivity;


/**
 * Implementation of App Widget functionality.
 */
public class StockWidget extends AppWidgetProvider {
    public static final String INTENT_ACTION = "com.sam_chordas.android.stockhawk.widget.graph";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stock_widget);
        setRemoteAdapter(context, views);

        Intent detailPageIntent = new Intent(context, StockGraphActivity.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                                                       .addNextIntentWithParentStack(detailPageIntent)
                                                       .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.recycler_view,clickPendingIntentTemplate);
        }
//        Intent graphIntent = new Intent(context,StockGraphActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,graphIntent,0);
//        views.setOnClickPendingIntent(R.id.recycler_view,pendingIntent);
//        Intent intent = new Intent(context,StockWidget.class);
//        intent.setAction(INTENT_ACTION);
//        views.setOnClickPendingIntent(R.id.recycler_view, PendingIntent.getBroadcast(context, 0, intent, 0));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(INTENT_ACTION.equals(intent.getAction())){
            Intent graphIntent = new Intent(context,StockWidget.class);
            graphIntent.putExtra("symbol",intent.getStringExtra("symbol"));
            graphIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(graphIntent);

        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static void setRemoteAdapter(Context context, RemoteViews views) {
        views.setRemoteAdapter(R.id.recycler_view,
                new Intent(context, WidgetService.class));
    }
}

