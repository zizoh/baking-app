package com.zizohanto.bakingapp.data.database.recipe;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    LiveData<List<RecipeResponse>> getAllRecipes();

    // Insert recipes
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<RecipeResponse> recipes);

    //
    @Query("DELETE FROM recipe")
    void deleteAllRecipes();

}
