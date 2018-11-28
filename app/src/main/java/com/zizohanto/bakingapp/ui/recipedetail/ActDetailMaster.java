package com.zizohanto.bakingapp.ui.recipedetail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zizohanto.bakingapp.IngredientWidgetProvider;
import com.zizohanto.bakingapp.IngredientWidgetService;
import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.ingredient.Ingredient;
import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;
import com.zizohanto.bakingapp.data.database.step.Step;
import com.zizohanto.bakingapp.ui.utils.StringUtils;

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
    public static final String ARG_STEP = "com.zizohanto.bakingapp.ui.recipedetail.arg_step";
    public static final String EXTRA_RECIPE = "com.zizohanto.bakingapp.ui.recipedetail.extra_recipe";

    private static final int REQUEST_RECIPE_RESPONSE = 1;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecipeResponse mRecipe;
    private RecipeStepDescriptionAdapter mRecipeStepDescriptionAdapter;
    private TextView tvRecipeIngredients;
    private SharedPreferences mSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail_master_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // TODO: Rename Activity title. Remove label tag in Activity's label tag in Manifest
        toolbar.setTitle(getTitle());

        tvRecipeIngredients = (TextView) findViewById(R.id.tv_recipe_ingredients);

        if (getIntent().hasExtra(EXTRA_RECIPE)) {
            mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRecipeStepDescriptionAdapter = new RecipeStepDescriptionAdapter(this);

        displayRecipeData(mRecipe);

        View recyclerView = findViewById(R.id.rv_step_description);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        setupSharedPreferences();
    }

    private void displayRecipeData(RecipeResponse recipeResponse) {
        if (recipeResponse != null) {
            mRecipeStepDescriptionAdapter.setStepsData(recipeResponse.getSteps());

            Context context = getBaseContext();
            String ingredientsListTitle = context.getString((R.string.ingredient_list_title));

            List<Ingredient> ingredients = recipeResponse.getIngredients();

            SpannableStringBuilder ssb = StringUtils.formatIngredientForTextViewDisplay(context, ingredientsListTitle, ingredients);

            tvRecipeIngredients.setText(ssb);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(mRecipeStepDescriptionAdapter);
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
