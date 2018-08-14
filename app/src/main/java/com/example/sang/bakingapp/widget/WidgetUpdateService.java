package com.example.sang.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.data.WidgetDataModel;

/**
 * Created by tashariko on 26/08/17.
 */

public class WidgetUpdateService extends IntentService {


    public static final String ACTION_UPDATE_APP_WIDGETS = "widgetupdateservice.update_app_widget";
    public static final String ACTION_UPDATE_LIST_VIEW = "widgetupdateservice.update_app_widget_list";

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_APP_WIDGETS.equals(action)) {
                handleActionUpdateAppWidgets();
            }else if(ACTION_UPDATE_LIST_VIEW.equals(action)){
                handleActionUpdateListView();
            }
        }
    }

    private void handleActionUpdateListView() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakeWidgetProvider.class));

        BakeWidgetProvider.updateAllAppWidget(this, appWidgetManager,appWidgetIds);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listView);

    }

    private void handleActionUpdateAppWidgets() {


        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakeWidgetProvider.class));

        BakeWidgetProvider.updateAllAppWidget(this, appWidgetManager,appWidgetIds);
    }

    public static void startActionUpdateAppWidgets(Context context, boolean forListView) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        if(forListView){
            intent.setAction(ACTION_UPDATE_LIST_VIEW);
        }else {
            intent.setAction(ACTION_UPDATE_APP_WIDGETS);
        }
        context.startService(intent);
    }

}
