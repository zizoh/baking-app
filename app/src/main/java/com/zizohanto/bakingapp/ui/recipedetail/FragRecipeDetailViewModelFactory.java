package com.zizohanto.bakingapp.ui.recipedetail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.zizohanto.bakingapp.data.BakingAppRepository;

public class FragRecipeDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final int mRecipeId;
    private final int mStepId;
    private final BakingAppRepository mRepository;

    public FragRecipeDetailViewModelFactory(BakingAppRepository repository, int recipeId, int stepId) {
        mRepository = repository;
        mRecipeId = recipeId;
        mStepId = stepId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new FragRecipeDetailViewModel(mRepository, mRecipeId, mStepId);
    }
}
