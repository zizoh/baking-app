package com.zizohanto.bakingapp.ui.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;
import com.zizohanto.bakingapp.ui.recipes.ActRecipes;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ActDetailMaster}.
 */
@SuppressWarnings({"Convert2Lambda", "RedundantCast"})
public class ActDetailDetail extends FragmentActivity {
    private RecipeResponse mRecipeResponse;
    private int mClickedStepPosition;
    private int mNumberOfSteps;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
//        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        if (getIntent().hasExtra(ActDetailMaster.EXTRA_RECIPE)) {
            mRecipeResponse = getIntent().getParcelableExtra(ActDetailMaster.EXTRA_RECIPE);
            mClickedStepPosition = getIntent().getIntExtra(ActDetailMaster.EXTRA_CLICKED_STEP_POSITION, 0);
            mNumberOfSteps = mRecipeResponse.getSteps().size();
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            mPager = (ViewPager) findViewById(R.id.pager);

            mPagerAdapter = new StepSlidePagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);
            mPager.setCurrentItem(mClickedStepPosition);

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
            intent.putExtra(ActRecipes.EXTRA_RECIPE, mRecipeResponse);
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onBackPressed() {
////        if (mPager.getCurrentItem() == 0) {
////            // If the user is currently looking at the first step, allow the system to handle the
////            // Back button. This calls finish() on this activity and pops the back stack.
////            super.onBackPressed();
////        } else {
////            // Otherwise, select the previous step.
////            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
////        }
//    }

    private class StepSlidePagerAdapter extends FragmentStatePagerAdapter {

        public StepSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
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
