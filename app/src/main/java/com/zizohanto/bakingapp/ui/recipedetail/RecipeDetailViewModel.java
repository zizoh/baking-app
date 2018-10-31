package com.zizohanto.bakingapp.ui.recipedetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.zizohanto.bakingapp.data.BakingAppRepository;
import com.zizohanto.bakingapp.data.database.RecipeResponse;

public class RecipeDetailViewModel extends ViewModel {
    private final int mRecipeId;
    private final BakingAppRepository mRepository;
    private LiveData<RecipeResponse> mRecipeResponse;

    RecipeDetailViewModel(BakingAppRepository repository, int recipeId) {
        mRecipeId = recipeId;
        mRepository = repository;
        mRecipeResponse = mRepository.getRecipeById(mRecipeId);
    }

    public LiveData<RecipeResponse> getRecipeResponse() {
        return mRecipeResponse;
    }

}
