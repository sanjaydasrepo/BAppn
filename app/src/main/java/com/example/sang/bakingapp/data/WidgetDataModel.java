package com.example.sang.bakingapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.sang.bakingapp.modal.Ingredients;
import com.example.sang.bakingapp.modal.Recipe;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import static com.example.sang.bakingapp.BakingApplication.PREF_KEY_JSON;

public class WidgetDataModel {


    public static void createDataForWidget(Context context , Recipe recipe) throws IOException {
        SharedPreferences sharedPref = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString( recipe );
        sharedPref.edit().putString(PREF_KEY_JSON, json).apply();
    }

    public static Recipe getDataForWidget(Context context) throws IOException {
        SharedPreferences sharedPref = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        String json = sharedPref.getString(PREF_KEY_JSON, "[]");

        ObjectMapper mapper = new ObjectMapper();
        Recipe recipe = mapper.readValue( json , Recipe.class);
        return recipe;
    }

}
