package com.zizohanto.bakingapp.ui.recipedetail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.zizohanto.bakingapp.IngredientWidgetProvider;
import com.zizohanto.bakingapp.IngredientWidgetService;
import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.ingredient.Ingredient;
import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;
import com.zizohanto.bakingapp.data.database.step.Step;
import com.zizohanto.bakingapp.data.utils.InjectorUtils;
import com.zizohanto.bakingapp.data.utils.StringUtils;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ActDetailDetail} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@SuppressWarnings({"Convert2Lambda", "RedundantCast"})
public class ActDetailMaster extends AppCompatActivity implements RecipeStepDescriptionAdapter.StepClickListener {
    private static final String KEY_LIST_POSITION = "com.zizohanto.bakingapp.ui.recipedetail.key_lis_position";
    private static final String KEY_RECIPE_ID = "com.zizohanto.bakingapp.ui.recipedetail.key_recipe_id";
    public static final String ARG_STEP = "com.zizohanto.bakingapp.ui.recipedetail.arg_step";
    public static final String EXTRA_RECIPE = "com.zizohanto.bakingapp.ui.recipedetail.EXTRA_RECIPE";
    public static final String EXTRA_RECIPE_ID = "com.zizohanto.bakingapp.ui.recipedetail.EXTRA_RECIPE_ID";

    private static final int REQUEST_RECIPE_RESPONSE = 1;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private int mRecipeId;
    private Parcelable mListState;

    private RecipeResponse mRecipe;
    private RecipeDetailViewModel mViewModel;
    private RecipeStepDescriptionAdapter mRecipeStepDescriptionAdapter;
    private TextView tvRecipeIngredients;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Toolbar mToolbar;
    private SharedPreferences mSharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail_master_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        tvRecipeIngredients = (TextView) findViewById(R.id.tv_recipe_ingredients);

        if (getIntent() != null) {
            mRecipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(KEY_LIST_POSITION);
            mRecipeId = savedInstanceState.getInt(KEY_RECIPE_ID);
        }

        mRecipeStepDescriptionAdapter = new RecipeStepDescriptionAdapter(this);

        setupViewModel();
        getRecipe();

        mRecyclerView = findViewById(R.id.rv_step_description);
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);
        setupSharedPreferences();
    }

    private void setupViewModel() {
        ActDetailMasterViewModelFactory factory =
                InjectorUtils.provideADMViewModelFactory(this, mRecipeId);
        mViewModel = ViewModelProviders.of(this, factory).get(RecipeDetailViewModel.class);
    }

    private void getRecipe() {
        mViewModel.getRecipe().observe(this, new Observer<RecipeResponse>() {
            @Override
            public void onChanged(@Nullable RecipeResponse recipeResponse) {
                if (recipeResponse != null) {
                    mRecipe = recipeResponse;
                    if (mToolbar != null) {
                        mToolbar.setTitle(mRecipe.getName());
                    }
                    displayRecipeData(mRecipe);
                    if (mListState != null) {
                        mLayoutManager.onRestoreInstanceState(mListState);
                    }
                }
            }
        });
    }

    private void displayRecipeData(RecipeResponse recipeResponse) {
        if (recipeResponse != null) {
            mRecipeStepDescriptionAdapter.setStepsData(recipeResponse.getSteps());

            Context context = getBaseContext();
            String ingredientsListTitle = context.getString((R.string.ingredient_list_title));

            List<Ingredient> ingredients = recipeResponse.getIngredients();

            SpannableStringBuilder ssb = StringUtils.formatIngredientForTextViewDisplay(context,
                    ingredientsListTitle, ingredients);

            tvRecipeIngredients.setText(ssb);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(mRecipeStepDescriptionAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void setupSharedPreferences() {
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onStepClick(Step clickedStep, int clickedStepPosition) {

        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ARG_STEP, clickedStep);

            FragRecipeDetail fragment = new FragRecipeDetail();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, ActDetailDetail.class);
            intent.putExtra(ActDetailDetail.EXTRA_RECIPE, mRecipe);
            intent.putExtra(ActDetailDetail.EXTRA_CLICKED_STEP_POSITION, clickedStepPosition);

            startActivityForResult(intent, REQUEST_RECIPE_RESPONSE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(KEY_LIST_POSITION, mLayoutManager.onSaveInstanceState());
        outState.putInt(KEY_RECIPE_ID, mRecipeId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.act_detail_master_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.widget_menu) {
            saveToPref();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveToPref() {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putInt(getString(R.string.pref_key_recipe_id), mRecipe.getId());
        editor.apply();

        IngredientWidgetProvider.updateIngredientWidgets(this, mRecipe);
        IngredientWidgetService.startActionUpdateIngredientWidget(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_RECIPE_RESPONSE && resultCode == RESULT_OK) {
            mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
            displayRecipeData(mRecipe);
        }
    }
}
