package com.zizohanto.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zizohanto.bakingapp.data.BakingAppRepository;
import com.zizohanto.bakingapp.data.database.recipe.RecipeResponse;
import com.zizohanto.bakingapp.data.utils.InjectorUtils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */

@SuppressWarnings("Convert2Lambda")
public class IngredientWidgetService extends IntentService {
    private static final String ACTION_UPDATE_INGREDIENT_WIDGET = "com.zizohanto.bakingapp.action.update_ingredient_widget";

    private static final String EXTRA_RECIPE = "com.zizohanto.bakingapp.extra.RECIPE";

    public IngredientWidgetService() {
        super("IngredientWidgetService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateIngredientWidget(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int recipeId = sharedPreferences.getInt(context.getString(R.string.pref_key_recipe_id), 0);

        BakingAppRepository bakingAppRepository = InjectorUtils.provideRepository(context);
        AppExecutors executors = AppExecutors.getInstance();
        executors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                RecipeResponse recipe = bakingAppRepository.getRecipeWithId(recipeId);

                Intent intent = new Intent(context, IngredientWidgetService.class);
                intent.setAction(ACTION_UPDATE_INGREDIENT_WIDGET);
                intent.putExtra(EXTRA_RECIPE, recipe);
                context.startService(intent);
            }
        });
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_UPDATE_INGREDIENT_WIDGET.equals(action)) {
                RecipeResponse recipe = intent.getParcelableExtra(EXTRA_RECIPE);
                handleActionUpdateIngredientWidget(recipe);
            }
        }
    }

    /**
     * Handle action update ingredient widget in the provided background thread with the provided
     * parameters.
     *
     * @param recipe the recipe to be displayed in the widget
     */
    private void handleActionUpdateIngredientWidget(RecipeResponse recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientWidgetProvider.class));

        // Trigger data update to handle the widget and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredient_text);

        // Update all widgets
        IngredientWidgetProvider.updateIngredientWidgets(this, recipe);
    }
}
