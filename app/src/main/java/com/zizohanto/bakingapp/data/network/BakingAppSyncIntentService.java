package com.zizohanto.bakingapp.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.zizohanto.bakingapp.data.utils.InjectorUtils;

import timber.log.Timber;

public class BakingAppSyncIntentService extends IntentService {

    public BakingAppSyncIntentService() {
        super("BakingAppSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.d("Recipe Intent service started");

        NetworkDataSource networkDataSource =
                InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchRecipes();
    }
}
