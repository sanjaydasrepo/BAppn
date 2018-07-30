package com.example.sang.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.data.RequestInterface;
import com.example.sang.bakingapp.modal.Recipe;

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

    @BindView( R.id.rv_baking_list)
    RecyclerView rvBakingList;
    private RecipeAdapter mRecipeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind( this );

        numberOfColumns = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 5 : 1;

        GridLayoutManager layoutManager = new GridLayoutManager(this , numberOfColumns);
        rvBakingList.setLayoutManager(layoutManager);

        mRecipeAdapter = new RecipeAdapter( this );
        rvBakingList.setAdapter( mRecipeAdapter );
        loadJson();

    }

    public void loadJson() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<ArrayList<Recipe>> call = request.getJSON();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                ArrayList<Recipe> jsonResponse = response.body();

                if (jsonResponse != null ) {

                    mRecipeAdapter.setData( jsonResponse );
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
        Log.d("Click  " ,recipe.getName());
        Intent intent = new Intent( this , ItemListActivity.class);
        intent.putExtra( RECIPE_KEY , recipe );
        startActivity( intent );
    }
}
