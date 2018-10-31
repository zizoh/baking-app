package com.zizohanto.bakingapp.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import timber.log.Timber;

@Database(entities = {RecipeResponse.class}, version = 1)
@TypeConverters({IngredientListTypeConverter.class, StepListTypeConverter.class})
public abstract class BakingAppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "baking app db";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static BakingAppDatabase sInstance;

    public static BakingAppDatabase getInstance(Context context) {
        Timber.d("Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        BakingAppDatabase.class, BakingAppDatabase.DATABASE_NAME).build();
                Timber.d("Made new database");
            }
        }
        return sInstance;
    }

    // The associated DAO for the database
    public abstract RecipeDao recipeDao();

}
