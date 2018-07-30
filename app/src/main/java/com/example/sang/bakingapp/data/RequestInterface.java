package com.example.sang.bakingapp.data;

import com.example.sang.bakingapp.modal.JsonResponse;
import com.example.sang.bakingapp.modal.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<Recipe>> getJSON();
}
