package com.example.sang.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.modal.Recipe;
import com.example.sang.bakingapp.modal.Steps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Recipe recipe;
    private static final String RECIPE_KEY = "recipe_key";
    private static final String RECIPE_STEPS_KEY = "recipe_steps_key";
    private static final String INGREDIENTS_KEY = "ingredients_key";
    private static final String RECIPE_NAME_KEY = "recipe_name_key";
    public static final String SCREEN_TYPE = "screenType";
    public static final String TYPE_PORTRAIT = "portrait";
    public static final String TYPE_HORIZONTAL = "horizontal";



    @BindView(R.id.item_list)
    RecyclerView recyclerView;

    @BindView(R.id.btn_ingredient)
    Button btnIngredients;

    @BindView(R.id.tv_list_recipe_name)
    TextView tvRecipeName;

    @Nullable @BindView(R.id.btn_nav_back)
    Button btnNavBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        ButterKnife.bind( this );
        Intent intent = getIntent();

        recipe = intent.getParcelableExtra(RECIPE_KEY);


        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }


        assert recyclerView != null;
        setupRecyclerView( recyclerView );
        setupDataView( );

        btnIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), IngredientActivity.class);
                intent.putParcelableArrayListExtra(INGREDIENTS_KEY , (ArrayList<? extends Parcelable>) recipe.getIngredients());
                v.getContext().startActivity( intent );
            }
        });

        if( btnNavBack != null ){
            btnNavBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemListActivity.super.onBackPressed();
                }
            });
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString( RECIPE_NAME_KEY , recipe.getName());

    }


    private void setupDataView() {
        tvRecipeName.setText( recipe.getName() );
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, recipe.getSteps(), mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ItemListActivity mParentActivity;
        private final List<Steps> mValues;
        private final boolean mTwoPane;

        SimpleItemRecyclerViewAdapter(ItemListActivity parent,List<Steps> items,boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle arguments = new Bundle();
                Steps steps = (Steps) view.getTag();

                if (mTwoPane) {
                    arguments.putParcelable(RECIPE_STEPS_KEY ,steps );
                    arguments.putString( SCREEN_TYPE , TYPE_PORTRAIT);

                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments( arguments );
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();


                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(RECIPE_STEPS_KEY , steps);
                    context.startActivity( intent );
                }
            }
        };


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            int p = position + 1;
            holder.mIdView.setText( p+"" );
            holder.mContentView.setText(mValues.get(position).getShortDescription());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener( mOnClickListener );
         }

        @Override
        public int getItemCount() {
            if ( mValues != null)
                return mValues.size();

            return 0 ;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.id_text)
             TextView mIdView;

            @BindView(R.id.content)
             TextView mContentView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this , view);
            }

        }
    }
}
