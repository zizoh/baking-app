package com.zizohanto.bakingapp.ui.recipedetail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.zizohanto.bakingapp.data.BakingAppRepository;

public class RecipeDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final int mRecipeId;
    private final BakingAppRepository mRepository;

    public RecipeDetailViewModelFactory(BakingAppRepository repository, int recipeId) {
        mRepository = repository;
        mRecipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipeDetailViewModel(mRepository, mRecipeId);
    }
}
