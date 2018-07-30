package com.example.sang.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class BakingContract implements BaseColumns {
    public static final String CONTENT_AUTHORITY = "com.example.sang.bakingapp";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_BAKING = "baking";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(PATH_BAKING)
            .build();

    public static final String TABLE_RECIPE = "recipe";

    public static final String COLUMN_RECIPE_ID = "recipe_id";

    public static final String COLUMN_RECIPE_NAME = "recipe_name";

    public static final String COLUMN_SERVING = "serving";
    public static final String COLUMN_RECIPE_IMAGE = "recipe_image";


    public static final String TABLE_INGREDIENTS = "ingredients";

    public static final String COLUMN_QUANTITY= "quantity";

    public static final String COLUMN_MEASURE = "measure";

    public static final String COLUMN_INGREDIENT = "ingredient";
    public static final String COLUMN_INGREDIENT_RECIPE_ID = "ingredient_recipe_id";


    public static final String TABLE_RECIPE_STEPS = "recipe_steps";
    public static final String COLUMN_STEP_ID= "step_id";

    public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_THUMBNAIL = "thumbnail";
    public static final String COLUMN_STEP_RECIPE_ID = "recipe_id";




    public static Uri buildMovieUriWithId(long id) {
        return CONTENT_URI.buildUpon()
                .appendPath(Long.toString(id))
                .build();
    }
}
