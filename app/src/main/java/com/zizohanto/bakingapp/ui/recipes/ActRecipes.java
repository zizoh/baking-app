package com.zizohanto.bakingapp.ui.recipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;
import com.zizohanto.bakingapp.data.utils.InjectorUtils;
import com.zizohanto.bakingapp.data.utils.NetworkState;
import com.zizohanto.bakingapp.idlingResource.RecipesIdlingResource;
import com.zizohanto.bakingapp.ui.recipedetail.ActDetailMaster;

import java.util.List;

@SuppressWarnings({"Convert2Lambda", "RedundantCast"})
public class ActRecipes extends AppCompatActivity implements RecipeAdapter.RecipeItemClickListener {

    private static final String KEY_LIST_POSITION = "com.zizohanto.bakingapp.ui.recipes.KEY_LIST_POSITION";

    private boolean isLoading;

    private RecipesActViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    private Parcelable mListState;

    private GridLayoutManager mLayoutManager;
    private ScrollChildSwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    private RecipesIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recipes);

        mSwipeRefreshLayout = (ScrollChildSwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recipes);
        mRecyclerView.setLayoutManager(getLayoutManager());

        mAdapter = new RecipeAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(KEY_LIST_POSITION);
        }

        setProgressIndicator();

        setupViewModel();
        observeRecipes();
        observeNetworkState();
    }

    private GridLayoutManager getLayoutManager() {
        if (this.mRecyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(this, 2);
        } else if (this.mRecyclerView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLayoutManager = new GridLayoutManager(this, 4);
        }
        return mLayoutManager;
    }

    private void setupViewModel() {
        ActRecipesViewModelFactory factory =
                InjectorUtils.provideARViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, factory).get(RecipesActViewModel.class);
    }

    private void observeRecipes() {
        mViewModel.getRecipes().observe(this, new Observer<List<RecipeResponse>>() {
            @Override
            public void onChanged(@Nullable List<RecipeResponse> recipeResponses) {
                if (recipeResponses != null && recipeResponses.size() != 0) {
                    loading(false);
                    mAdapter.setRecipeData(recipeResponses);
                    if (mListState != null) {
                        mLayoutManager.onRestoreInstanceState(mListState);
                    }
                }
            }
        });
    }

    private void observeNetworkState() {
        mViewModel.getNetworkState().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if (networkState != null) {
                    if (networkState.getStatus() == NetworkState.Status.RUNNING) {
                        loading(true);
                    } else if (networkState.getStatus() == NetworkState.Status.FAILED) {
                        loading(false);
                        if (!isConnected()) {
                            Toast.makeText(getBaseContext(), "No internet." +
                                    "\nPlease connect to internet and try again", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getBaseContext(), networkState.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    public void loading(boolean loading) {
        isLoading = loading;
        setLoadingIndicator(loading);
    }

    private void setProgressIndicator() {
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout.
        mSwipeRefreshLayout.setScrollUpChild(mRecyclerView);

        setLoadingIndicator(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading) {
                    observeRecipes();
                }
            }
        });
    }

    public void setLoadingIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onRecipeClick(RecipeResponse clickedRecipe) {
        Intent intent = new Intent(this, ActDetailMaster.class);
        intent.putExtra(ActDetailMaster.EXTRA_RECIPE_ID, clickedRecipe.getId());
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(KEY_LIST_POSITION, mLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new RecipesIdlingResource();
        }
        return mIdlingResource;
    }
}
