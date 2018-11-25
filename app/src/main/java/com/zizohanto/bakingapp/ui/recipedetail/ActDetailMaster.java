package com.zizohanto.bakingapp.ui.recipedetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.ingredient.Ingredient;
import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;
import com.zizohanto.bakingapp.data.database.step.Step;
import com.zizohanto.bakingapp.ui.recipes.ActRecipes;

import java.util.List;
import java.util.Locale;

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
    public static final String ARG_STEP = "com.zizohanto.bakingapp.ui.recipedetail.ARG_STEP";
    public static final String EXTRA_RECIPE = "com.zizohanto.bakingapp.ui.recipedetail.RECIPE";

    private static final int REQUEST_RECIPE_RESPONSE = 1;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecipeResponse mRecipeResponse;
    private RecipeStepDescriptionAdapter mRecipeStepDescriptionAdapter;
    private TextView tvRecipeIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail_master_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // TODO: Rename Activity title. Remove label tag in Activity's label tag in Manifest
        toolbar.setTitle(getTitle());

        tvRecipeIngredients = (TextView) findViewById(R.id.tv_recipe_ingredients);

        if (getIntent().hasExtra(ActRecipes.EXTRA_RECIPE)) {
            mRecipeResponse = getIntent().getParcelableExtra(ActRecipes.EXTRA_RECIPE);
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRecipeStepDescriptionAdapter = new RecipeStepDescriptionAdapter(this);

        displayRecipeData(mRecipeResponse);

        View recyclerView = findViewById(R.id.rv_step_description);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void displayRecipeData(RecipeResponse recipeResponse) {
        if (recipeResponse != null) {
            mRecipeStepDescriptionAdapter.setStepsData(recipeResponse.getSteps());

            Context context = getBaseContext();
            String ingredientsListTitle = context.getString((R.string.ingredient_list_title));
            StringBuilder sb = new StringBuilder();
            sb.append(ingredientsListTitle);

            List<Ingredient> ingredients = recipeResponse.getIngredients();
            for (Ingredient ingredient : ingredients) {
                String name = ingredient.getIngredient();
                double quantity = ingredient.getQuantity();
                String measure = ingredient.getMeasure();

                sb.append("\n");
                sb.append(formatIngredient(context, name, quantity, measure));
            }

            SpannableStringBuilder ssb = buildSpannableString(sb.toString(), ingredientsListTitle,
                    new StyleSpan(Typeface.BOLD));
            tvRecipeIngredients.setText(ssb);
        }
    }

    public String formatIngredient(Context context, String name, double quantity, String measure) {

        String line = context.getString(R.string.recipe_details_ingredient_line);

        String quantityStr = String.format(Locale.US, "%s", quantity);
        if (quantity == (long) quantity) {
            quantityStr = String.format(Locale.US, "%d", (long) quantity);
        }

        return String.format(Locale.US, line, name, quantityStr, measure);
    }

    public SpannableStringBuilder buildSpannableString(String fullText, String styledText, StyleSpan style) {
        SpannableStringBuilder sb = new SpannableStringBuilder(fullText);
        int start = fullText.indexOf(styledText);
        int end = start + styledText.length();
        sb.setSpan(style, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(mRecipeStepDescriptionAdapter);
    }

    @Override
    public void onStepClick(Step clickedStep) {

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
            intent.putExtra(ARG_STEP, clickedStep);
            intent.putExtra(EXTRA_RECIPE, mRecipeResponse);

            startActivityForResult(intent, REQUEST_RECIPE_RESPONSE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_RECIPE_RESPONSE && resultCode == RESULT_OK) {
            mRecipeResponse = getIntent().getParcelableExtra(EXTRA_RECIPE);
            displayRecipeData(mRecipeResponse);
        }
    }
}
