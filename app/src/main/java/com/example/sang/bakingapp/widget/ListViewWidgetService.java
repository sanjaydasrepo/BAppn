package com.example.sang.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.data.WidgetDataModel;
import com.example.sang.bakingapp.modal.Ingredients;
import com.example.sang.bakingapp.modal.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListViewWidgetService extends RemoteViewsService {
    private static final String FROM_TITLE_TEXT = "title-text";


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Recipe recipe = null;
        try {
            recipe = WidgetDataModel.getDataForWidget(getApplicationContext());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new AppWidgetListView( this.getApplicationContext() , recipe );
    }

    class AppWidgetListView implements RemoteViewsService.RemoteViewsFactory{

        private Context context;
        private List<Ingredients> list;
        Recipe recipe;

        public AppWidgetListView(Context applicationContext, Recipe recipe) {
            context = applicationContext;
            this.recipe = recipe;
            list = recipe.getIngredients() ;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item_widget);

            views.setTextViewText(R.id.tv_ingredient , list.get(position).getIngredient());
            views.setTextViewText(R.id.tv_measure , list.get(position).getMeasure());
            views.setTextViewText(R.id.tv_quantity , list.get(position).getQuantity());

            Intent fillInIntent = new Intent();
            fillInIntent.putExtra(FROM_TITLE_TEXT , recipe);
            views.setOnClickFillInIntent(R.id.parentView , fillInIntent);
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
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
