package com.zizohanto.bakingapp.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.zizohanto.bakingapp.AppExecutors;
import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;
import com.zizohanto.bakingapp.data.utils.NetworkState;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Provides an API for doing all operations with the server data
 */
@SuppressWarnings("Convert2Lambda")
public class NetworkDataSource {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static NetworkDataSource sInstance;
    private final Context mContext;
    private final AppExecutors mExecutors;

    // LiveData storing the downloaded recipes data
    private final MutableLiveData<List<RecipeResponse>> mDownloadedRecipes;

    private final MutableLiveData<NetworkState> networkState;

    private NetworkDataSource(@NonNull Context context, AppExecutors executors) {
        mContext = context;
        mExecutors = executors;
        mDownloadedRecipes = new MutableLiveData<>();
        networkState = new MutableLiveData<>();
    }

    /**
     * Get the singleton for this class
     */
    public static NetworkDataSource getInstance(Context context, AppExecutors executors) {
        Timber.d("Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new NetworkDataSource(context.getApplicationContext(), executors);
                Timber.d("Made new network data source");
            }
        }
        return sInstance;
    }

    public LiveData<List<RecipeResponse>> getRecipesData() {
        return mDownloadedRecipes;
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    /**
     * Starts an intent service to fetch the recipes.
     */
    public void startFetchRecipesService() {
        Intent intentToFetch = new Intent(mContext, BakingAppSyncIntentService.class);
        mContext.startService(intentToFetch);
        Timber.d("Recipes Service created");
    }

    /**
     * Get recipes
     */
    void fetchRecipes() {
        mExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                networkState.postValue(NetworkState.LOADING);

                ApiInterface apiService = ApiClient.getClient();
                Call<List<RecipeResponse>> call = apiService.getRecipes();
                call.enqueue(new Callback<List<RecipeResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<RecipeResponse>> call, @NonNull Response<List<RecipeResponse>> response) {
                        Timber.d("got a response %s", response);
                        if (response.isSuccessful()) {
                            Timber.d("Recipe received");
                            mDownloadedRecipes.postValue(response.body());
                            networkState.postValue(NetworkState.LOADED);
                        } else {
                            Timber.d("%sUnknown error", String.valueOf(response.errorBody()));
                            networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<RecipeResponse>> call, @NonNull Throwable t) {
                        Timber.e(t.toString());
                        networkState.postValue(new NetworkState(NetworkState.Status.FAILED, t.getMessage()));
                    }
                });
            }
        });
    }
}
