package com.zizohanto.bakingapp.ui.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ActDetailMaster}.
 */
@SuppressWarnings({"Convert2Lambda", "RedundantCast"})
public class ActDetailDetail extends FragmentActivity {
    public static final String KEY_CLICKED_POSITION = "com.zizohanto.bakingapp.ui.recipedetail.key_clickec_position";
    public static final String KEY_NUMBER_OF_STEPS = "com.zizohanto.bakingapp.ui.recipedetail.key_number_of_steps";
    public static final String KEY_RECIPE = "com.zizohanto.bakingapp.ui.recipedetail.key_recipe";
    public static final String EXTRA_RECIPE = "com.zizohanto.bakingapp.ui.recipedetail.extra_recipe";
    public static final String EXTRA_CLICKED_STEP_POSITION = "com.zizohanto.bakingapp.ui.recipedetail.clicked_step_position";

    private RecipeResponse mRecipeResponse;

    private int mClickedStepPosition;
    private int mNumberOfSteps;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail_detail);

        if (getIntent().hasExtra(EXTRA_RECIPE) && getIntent().hasExtra(EXTRA_CLICKED_STEP_POSITION)) {
            mRecipeResponse = getIntent().getParcelableExtra(EXTRA_RECIPE);
            mClickedStepPosition = getIntent().getIntExtra(EXTRA_CLICKED_STEP_POSITION, 0);
        }

        mPager = (ViewPager) findViewById(R.id.pager);

        // Get the number of Steps so that the adapter will know how many pages it will be displaying
        mNumberOfSteps = mRecipeResponse.getSteps().size();
        mPagerAdapter = new StepSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mClickedStepPosition);

        if (savedInstanceState != null) {
            mClickedStepPosition = savedInstanceState.getInt(KEY_CLICKED_POSITION);
            mNumberOfSteps = savedInstanceState.getInt(KEY_NUMBER_OF_STEPS);
            mRecipeResponse = savedInstanceState.getParcelable(KEY_RECIPE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent = new Intent(this, ActDetailMaster.class);
            intent.putExtra(ActDetailMaster.EXTRA_RECIPE_ID, mRecipeResponse.getId());
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_CLICKED_POSITION, mClickedStepPosition);
        outState.putInt(KEY_NUMBER_OF_STEPS, mNumberOfSteps);
        outState.putParcelable(KEY_RECIPE, mRecipeResponse);
        super.onSaveInstanceState(outState);
    }

    private class StepSlidePagerAdapter extends FragmentStatePagerAdapter {

        StepSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // Create the detail fragment and return it to the activity
            Bundle arguments = new Bundle();
            arguments.putParcelable(ActDetailMaster.ARG_STEP, mRecipeResponse.getSteps().get(position));

            FragRecipeDetail fragment = new FragRecipeDetail();
            fragment.setArguments(arguments);
            return fragment;
        }

        @Override
        public int getCount() {
            return mNumberOfSteps;
        }
    }

}
