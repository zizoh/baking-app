package com.zizohanto.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.zizohanto.bakingapp.AppExecutors;
import com.zizohanto.bakingapp.data.database.recipe.RecipeDao;
import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;
import com.zizohanto.bakingapp.data.network.NetworkDataSource;
import com.zizohanto.bakingapp.data.utils.NetworkState;

import java.util.List;

import timber.log.Timber;

/**
 * Handles data operations in BakingApp. Acts as a mediator between {@link NetworkDataSource}
 * and {@link RecipeDao}
 */
@SuppressWarnings("Convert2Lambda")
public class BakingAppRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static BakingAppRepository sInstance;
    private final RecipeDao mRecipeDao;
    private final NetworkDataSource mNetworkDataSource;
    private final AppExecutors mExecutors;

    private boolean mInitialized = false;

    private BakingAppRepository(RecipeDao recipeDao,
                                NetworkDataSource networkDataSource,
                                AppExecutors executors) {
        mRecipeDao = recipeDao;
        mNetworkDataSource = networkDataSource;
        mExecutors = executors;

        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<List<RecipeResponse>> networkData = mNetworkDataSource.getRecipesData();

        networkData.observeForever(new Observer<List<RecipeResponse>>() {
            @Override
            public void onChanged(@Nullable List<RecipeResponse> newRecipesFromNetwork) {
                mExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        // Deletes old historical data
                        deleteOldRecipeData();

                        // Insert our new recipe data into Baking App's database
                        mRecipeDao.insert(newRecipesFromNetwork);
                        Timber.d("New values inserted");
                    }
                });
            }
        });
    }

    public synchronized static BakingAppRepository getInstance(
            RecipeDao recipeDao,
            NetworkDataSource networkDataSource,
            AppExecutors executors) {
        Timber.d("Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new BakingAppRepository(recipeDao, networkDataSource, executors);
                Timber.d("Made new repository");
            }
        }
        return sInstance;
    }

    /**
     * Deletes old recipes data after new recipes are fetched successfully
     */
    private void deleteOldRecipeData() {
        mRecipeDao.deleteAllRecipes();
        Timber.d("Old recipes deleted");
    }

    /**
     * Starts service that fetches data
     */
    private synchronized void initializeData() {
        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, nothing is done in this method.
        if (mInitialized) return;
        mInitialized = true;

        startFetchRecipesService();
    }

    /**
     * Network related operation
     */
    private void startFetchRecipesService() {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mNetworkDataSource.startFetchRecipesService();
            }
        });
    }

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkDataSource.getNetworkState();
    }

    /*
     * Recipes database related operations
     */
    public LiveData<List<RecipeResponse>> getRecipes() {
        initializeData();
        return mRecipeDao.getAllRecipes();
    }
}
