package com.zizohanto.bakingapp.ui.recipes;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.zizohanto.bakingapp.data.BakingAppRepository;

/**
 * Factory method to create a ViewModel with a constructor that takes a
 * {@link BakingAppRepository}
 */
public class ActRecipesViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final BakingAppRepository mRepository;

    public ActRecipesViewModelFactory(BakingAppRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RecipesActViewModel(mRepository);
    }
}
