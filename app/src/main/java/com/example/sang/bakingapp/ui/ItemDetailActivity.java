package com.example.sang.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Button;

import com.example.sang.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.btn_nav_back)
    Button btnNavBack;

    ItemDetailFragment fragment;
    private final String FRAGMENT_KEY = "my-fragment";

    public static final String FULLSCREEN = "fullscreen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        ButterKnife.bind( this );



            Bundle arguments = new Bundle();
            arguments.putParcelable(ItemDetailFragment.RECIPE_STEPS_KEY,
                    getIntent().getParcelableExtra(ItemDetailFragment.RECIPE_STEPS_KEY));


            int orientation = getResources().getConfiguration().orientation;

            if( orientation == Configuration.ORIENTATION_PORTRAIT ){
                arguments.putString( ItemListActivity.SCREEN_TYPE ,
                        ItemListActivity.TYPE_PORTRAIT);

                btnNavBack.setVisibility(View.VISIBLE);
            }else{
                arguments.putString( ItemListActivity.SCREEN_TYPE ,
                        ItemListActivity.TYPE_HORIZONTAL);

                btnNavBack.setVisibility(View.GONE);
            }


        if( btnNavBack != null ) {
            btnNavBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemDetailActivity.super.onBackPressed();
                }
            });
        }


        if( orientation == Configuration.ORIENTATION_LANDSCAPE ){
            arguments.putBoolean(FULLSCREEN , true);
        }else{
            arguments.putBoolean(FULLSCREEN , false);
        }

        fragment = new ItemDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.item_detail_container, fragment)
                .commit();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            NavUtils.navigateUpTo(this,
                    new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
