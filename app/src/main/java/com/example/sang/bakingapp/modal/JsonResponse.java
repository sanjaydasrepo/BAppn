package com.example.sang.bakingapp.modal;

import java.util.ArrayList;
import java.util.List;

public class JsonResponse {


    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    public ArrayList<Recipe> recipeList;


}
