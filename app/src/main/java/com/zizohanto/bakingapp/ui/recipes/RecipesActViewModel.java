package com.zizohanto.bakingapp.ui.recipes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.zizohanto.bakingapp.data.BakingAppRepository;
import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;
import com.zizohanto.bakingapp.data.utils.NetworkState;

import java.util.List;

public class RecipesActViewModel extends ViewModel {
    private final BakingAppRepository mRepository;
    private LiveData<List<RecipeResponse>> mRecipes;
    private LiveData<NetworkState> mNetworkState;

    RecipesActViewModel(BakingAppRepository repository) {
        mRepository = repository;
        mRecipes = mRepository.getRecipes();
        mNetworkState = mRepository.getNetworkState();
    }

    public LiveData<List<RecipeResponse>> getRecipes() {
        return mRecipes;
    }

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkState;
    }
}
