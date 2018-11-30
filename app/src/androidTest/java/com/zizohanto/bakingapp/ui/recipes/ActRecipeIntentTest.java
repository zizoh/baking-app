package com.zizohanto.bakingapp.ui.recipes;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.ui.recipedetail.ActDetailMaster;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ActRecipeIntentTest {

    @Rule
    public IntentsTestRule<ActRecipes> intentsTestRule
            = new IntentsTestRule<>(ActRecipes.class);

    @Test
    public void clickOnRecyclerViewItem_runsActDetailMasterIntent() {
        onView(ViewMatchers.withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(
                hasExtra(ActDetailMaster.EXTRA_RECIPE_ID, 1)
        );
    }
}
