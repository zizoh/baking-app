package com.zizohanto.bakingapp.ui.recipes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.RecipeResponse;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private List<RecipeResponse> mRecipes;
    private RecipeItemClickListener mOnClickListener;

    RecipeAdapter(Context context, RecipeItemClickListener listener) {
        mOnClickListener = listener;
    }


    @NonNull
    @Override
    public RecipeAdapter.RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                    int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.recipes_list_content, parent, false);

        return new RecipeAdapterViewHolder(view);

    }

    @Override
    public int getItemCount() {
        if (null == mRecipes) return 0;
        return mRecipes.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeAdapterViewHolder holder, int position) {
        holder.bind(mRecipes.get(position));
    }

    void setRecipeData(List<RecipeResponse> newRecipes) {
        // If there was no recipe data, then recreate all of the list
        if (mRecipes == null) {
            mRecipes = newRecipes;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mRecipes.size();
                }

                @Override
                public int getNewListSize() {
                    return newRecipes.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mRecipes.get(oldItemPosition).getId() ==
                            newRecipes.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    RecipeResponse newRecipe = newRecipes.get(newItemPosition);
                    RecipeResponse oldRecipe = mRecipes.get(oldItemPosition);
                    return newRecipe.getId() == oldRecipe.getId()
                            && newRecipe.getName().equals(oldRecipe.getName());
                }
            });
            mRecipes = newRecipes;
            result.dispatchUpdatesTo(this);
        }
    }

    public interface RecipeItemClickListener {
        void onRecipeClick(RecipeResponse clickedRecipe);
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mRecipeName;
        private TextView mRecipeServings;

        private RecipeAdapterViewHolder(View itemView) {
            super(itemView);

            mRecipeName = itemView.findViewById(R.id.tv_recipe_name);
            mRecipeServings = itemView.findViewById(R.id.tv_recipe_servings);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onRecipeClick(mRecipes.get(clickedPosition));
        }

        void bind(RecipeResponse recipeResponse) {
            mRecipeName.setText(recipeResponse.getName());
            mRecipeServings.setText(String.format("Servings: %s", String.valueOf(recipeResponse.getServings())));
        }
    }
}
