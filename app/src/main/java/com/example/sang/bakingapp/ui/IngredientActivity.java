package com.example.sang.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.modal.Ingredients;
import com.example.sang.bakingapp.modal.Steps;
import com.example.sang.bakingapp.utils.BakingUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientActivity extends AppCompatActivity {

    @BindView(R.id.ingredient_item_list)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.btn_nav_back)
    Button btnNavBack;


    private List<Ingredients> ingredientsList;
    private static final String INGREDIENTS_KEY = "ingredients_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        ButterKnife.bind( this );

        Intent intent = getIntent();
        ingredientsList = intent.getParcelableArrayListExtra( INGREDIENTS_KEY );

        setupRecyclerView( recyclerView );

        if( btnNavBack != null ){
            btnNavBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IngredientActivity.super.onBackPressed();
                }
            });
        }


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new IngredientActivity.SimpleItemRecyclerViewAdapter(this, ingredientsList));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<IngredientActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final IngredientActivity mParentActivity;
        private final List<Ingredients> mValues;

        SimpleItemRecyclerViewAdapter(IngredientActivity parent,List<Ingredients> items) {
            mValues = items;
            mParentActivity = parent;
        }


        @Override
        public IngredientActivity.SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_item_list_content, parent, false);
            return new IngredientActivity.SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final IngredientActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
           holder.mTvIngredient.setText(BakingUtils.toTitleCase(mValues.get( position ).getIngredient() ));
           holder.mMeasure.setText( mValues.get(position).getMeasure() );
           holder.mQuantity.setText( mValues.get(position).getQuantity() );
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            @BindView(R.id.tv_ingredient)
            TextView mTvIngredient;

            @BindView(R.id.tv_measure)
            TextView mMeasure;

            @BindView(R.id.tv_quantity)
            TextView mQuantity;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this , view);
            }

        }
    }
}
