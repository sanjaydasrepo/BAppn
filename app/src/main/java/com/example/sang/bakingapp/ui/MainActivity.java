package com.example.sang.bakingapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sang.bakingapp.IdilingResource.SimpleIdlingResource;
import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.data.RequestInterface;
import com.example.sang.bakingapp.data.WidgetDataModel;
import com.example.sang.bakingapp.modal.Recipe;
import com.example.sang.bakingapp.utils.BakingUtils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClick {

    private static final String BASE_URL="https://d17h27t6h515a5.cloudfront.net";
    private int numberOfColumns;
    private static final String RECIPE_KEY = "recipe_key";
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @BindView( R.id.rv_baking_list)
    RecyclerView rvBakingList;

    @BindView( R.id.tv_error_message_display )
    TextView mErrorMessageDisplay;

    private RecipeAdapter mRecipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind( this );



        numberOfColumns = BakingUtils.calculateNoOfColumns( this );

        GridLayoutManager layoutManager = new GridLayoutManager(this , numberOfColumns);
        rvBakingList.setLayoutManager(layoutManager);

        mRecipeAdapter = new RecipeAdapter( this );
        rvBakingList.setAdapter( mRecipeAdapter );

        if( BakingUtils.isOnline( this ) ) {

            loadJson();
        }else{
            showErrorMessage( R.string.network_error );
        }

    }

    private void showErrorMessage(int stringIndex) {
        /* First, hide the currently visible data */
        rvBakingList.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setText(getString(stringIndex));
    }

    private void showDataView() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        rvBakingList.setVisibility(View.VISIBLE);
    }




    public void loadJson() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<ArrayList<Recipe>> call = request.getJSON();

        SimpleIdlingResource idlingResource = (SimpleIdlingResource)getIdlingResource();

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                ArrayList<Recipe> jsonResponse = response.body();

                if (jsonResponse != null ) {
                    showDataView();
                    mRecipeAdapter.setData( jsonResponse );

                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent( this , ItemListActivity.class);
        intent.putExtra( RECIPE_KEY , recipe );
        try {
            WidgetDataModel.createDataForWidget( this , recipe);
        } catch (IOException e) {
            e.printStackTrace();
        }

        startActivity( intent );
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
