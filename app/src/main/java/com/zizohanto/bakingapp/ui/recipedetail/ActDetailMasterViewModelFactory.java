package com.zizohanto.bakingapp.ui.recipedetail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.zizohanto.bakingapp.data.BakingAppRepository;

public class ActDetailMasterViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final int mRecipeId;
    private final BakingAppRepository mRepository;

    public ActDetailMasterViewModelFactory(BakingAppRepository repository, int recipeId) {
        mRepository = repository;
        mRecipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ActDetailMasterViewModel(mRepository, mRecipeId);
    }
}
