package com.zizohanto.bakingapp.data.utils;

import android.content.Context;

import com.zizohanto.bakingapp.AppExecutors;
import com.zizohanto.bakingapp.data.BakingAppRepository;
import com.zizohanto.bakingapp.data.database.BakingAppDatabase;
import com.zizohanto.bakingapp.data.network.NetworkDataSource;
import com.zizohanto.bakingapp.ui.recipedetail.ActDetailMasterViewModelFactory;
import com.zizohanto.bakingapp.ui.recipedetail.FragRecipeDetailViewModelFactory;
import com.zizohanto.bakingapp.ui.recipes.ActRecipesViewModelFactory;

/**
 * Provides static methods to inject the various classes needed for Baking App
 */
public class InjectorUtils {
    public static BakingAppRepository provideRepository(Context context) {
        BakingAppDatabase database = BakingAppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        NetworkDataSource networkDataSource =
                NetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return BakingAppRepository.getInstance(database.recipeDao(), database.stepDao(), networkDataSource, executors);
    }

    public static NetworkDataSource provideNetworkDataSource(Context context) {
        provideRepository(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return NetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static ActDetailMasterViewModelFactory provideADMViewModelFactory(Context context, int recipeId) {
        BakingAppRepository repository = provideRepository(context.getApplicationContext());
        return new ActDetailMasterViewModelFactory(repository, recipeId);
    }

    public static ActRecipesViewModelFactory provideRAViewModelFactory(Context context) {
        BakingAppRepository repository = provideRepository(context);
        return new ActRecipesViewModelFactory(repository);
    }

    public static FragRecipeDetailViewModelFactory provideFRDViewModelFactory(Context context,
                                                                              int recipeId,
                                                                              int stepId) {
        BakingAppRepository repository = provideRepository(context);
        return new FragRecipeDetailViewModelFactory(repository, recipeId, stepId);
    }
}
