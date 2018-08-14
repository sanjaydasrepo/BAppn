package com.example.sang.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.data.WidgetDataModel;
import com.example.sang.bakingapp.modal.Recipe;
import com.example.sang.bakingapp.ui.IngredientActivity;
import com.example.sang.bakingapp.ui.ItemDetailActivity;
import com.example.sang.bakingapp.ui.ItemListActivity;
import com.example.sang.bakingapp.ui.MainActivity;

import java.io.IOException;

/**
 * Implementation of App Widget functionality.
 */
public class BakeWidgetProvider extends AppWidgetProvider {

    private static final String FROM_TITLE_TEXT = "title-text";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Recipe recipe = null;
        try {
            recipe = WidgetDataModel.getDataForWidget( context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_widget);

        Intent intent = new Intent( context , MainActivity.class);

        views.setTextViewText(R.id.titleTextView , recipe.getName());

        PendingIntent pendingIntent = PendingIntent.getActivity( context , 0 , intent , PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.titleTextView , pendingIntent);


        Intent listIntent = new Intent(context, ListViewWidgetService.class);
        views.setRemoteAdapter(R.id.listView, listIntent);

        Intent appIntent = new Intent(context, ItemListActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.listView, appPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WidgetUpdateService.startActionUpdateAppWidgets(context,false);
    }

    public static void updateAllAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
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
}

