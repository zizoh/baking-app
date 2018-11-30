package com.zizohanto.bakingapp.ui.recipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;
import com.zizohanto.bakingapp.data.utils.InjectorUtils;
import com.zizohanto.bakingapp.idlingResource.RecipesIdlingResource;
import com.zizohanto.bakingapp.ui.recipedetail.ActDetailMaster;

import java.util.List;

@SuppressWarnings({"Convert2Lambda", "RedundantCast"})
public class ActRecipes extends AppCompatActivity implements RecipeAdapter.RecipeItemClickListener {

    private RecipesActViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;

    @Nullable
    private RecipesIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recipes);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recipes);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecipeAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        setupViewModel();
        observeRecipes();
    }

    private void setupViewModel() {
        ActRecipesViewModelFactory factory =
                InjectorUtils.provideARViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, factory).get(RecipesActViewModel.class);
    }

    private void observeRecipes() {
        mViewModel.getRecipes().observe(this, new Observer<List<RecipeResponse>>() {
            @Override
            public void onChanged(@Nullable List<RecipeResponse> recipeResponses) {
                if (recipeResponses != null && recipeResponses.size() != 0) {
                    mAdapter.setRecipeData(recipeResponses);
                }
            }
        });
    }

    @Override
    public void onRecipeClick(RecipeResponse clickedRecipe) {
        Intent intent = new Intent(this, ActDetailMaster.class);
        intent.putExtra(ActDetailMaster.EXTRA_RECIPE_ID, clickedRecipe.getId());
        startActivity(intent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new RecipesIdlingResource();
        }
        return mIdlingResource;
    }
}
