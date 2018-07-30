package com.example.sang.bakingapp.utils;

import android.net.Uri;
import android.util.Log;

import com.example.sang.bakingapp.data.RequestInterface;
import com.example.sang.bakingapp.modal.Recipe;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {
    private static final String BASE_URL="https://d17h27t6h515a5.cloudfront.net";
    private static ArrayList<Recipe> recipeList;

    public static URL buildURL(String qryType){

        Uri uri = Uri.parse(qryType);

        URL url = null;
        try {
            url = new URL(uri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static ArrayList<Recipe> loadJson() {
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

                if (jsonResponse !=null ) {
                    recipeList = jsonResponse;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });

        return recipeList;

    }

}
