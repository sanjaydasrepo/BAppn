package com.example.sang.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sang.bakingapp.R;
import com.example.sang.bakingapp.modal.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeListViewHolder> {


    ArrayList<Recipe> recipeList;
    public OnRecipeClick onRecipeClickHandler;

    public RecipeAdapter(OnRecipeClick c) {
        this.onRecipeClickHandler = c;
    }


    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int idItem = R.layout.recipe_card;

        LayoutInflater inflater = LayoutInflater.from(context);

        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(idItem , parent , shouldAttachToParentImmediately);
        return new RecipeListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        holder.ivPoster.setText( recipeList.get(position).getName() );
    }

    @Override
    public int getItemCount() {
        if( recipeList == null)
            return 0;

        return recipeList.size();
    }

    public void setData(ArrayList<Recipe> recipes){
        this.recipeList = recipes ;
        notifyDataSetChanged();
    }

    interface OnRecipeClick{
        public void onRecipeClick( Recipe recipe );
    }

    class RecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_recipe_name)
        TextView ivPoster;


        public RecipeListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            Recipe r = recipeList.get(i);
            onRecipeClickHandler.onRecipeClick( r );

        }
    }

}
