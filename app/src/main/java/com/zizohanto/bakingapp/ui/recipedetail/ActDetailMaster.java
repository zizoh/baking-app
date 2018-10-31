package com.zizohanto.bakingapp.ui.recipedetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.RecipeResponse;
import com.zizohanto.bakingapp.data.utils.InjectorUtils;
import com.zizohanto.bakingapp.ui.recipes.ActRecipes;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ActDetailDetail} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@SuppressWarnings({"Convert2Lambda", "RedundantCast"})
public class ActDetailMaster extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private int mRecipeId;
    private RecipeDetailViewModel mViewModel;
    private RecipeStepDescriptionAdapter mRecipeStepDescriptionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail_master_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (getIntent().hasExtra(ActRecipes.EXTRA_RECIPE_ID)) {
            mRecipeId = getIntent().getIntExtra(ActRecipes.EXTRA_RECIPE_ID, 1);
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRecipeStepDescriptionAdapter = new RecipeStepDescriptionAdapter(this, mTwoPane);

        setupViewModel();
        observeRecipeResponse();

        View recyclerView = findViewById(R.id.rv_step_description);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupViewModel() {
        RecipeDetailViewModelFactory factory =
                InjectorUtils.provideRDViewModelFactory(this, mRecipeId);
        mViewModel = ViewModelProviders.of(this, factory).get(RecipeDetailViewModel.class);
    }

    private void observeRecipeResponse() {
        mViewModel.getRecipeResponse().observe(this, new Observer<RecipeResponse>() {
            @Override
            public void onChanged(@Nullable RecipeResponse recipeResponse) {
                if (recipeResponse != null) {
                    mRecipeStepDescriptionAdapter.setStepsData(recipeResponse.getSteps());
                }
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(mRecipeStepDescriptionAdapter);
    }

}