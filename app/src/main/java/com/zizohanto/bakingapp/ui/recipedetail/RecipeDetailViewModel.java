package com.zizohanto.bakingapp.ui.recipedetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.zizohanto.bakingapp.data.BakingAppRepository;
import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;

public class RecipeDetailViewModel extends ViewModel {
    private final BakingAppRepository mRepository;
    private LiveData<RecipeResponse> mRecipe;
    private int mRecipeId;

    RecipeDetailViewModel(BakingAppRepository repository, int recipeId) {
        mRepository = repository;
        mRecipeId = recipeId;
        mRecipe = mRepository.getLiveDataRecipeWithId(mRecipeId);
    }

    public LiveData<RecipeResponse> getRecipe() {
        return mRecipe;
    }
}
