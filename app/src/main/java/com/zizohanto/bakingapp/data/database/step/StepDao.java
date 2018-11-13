package com.zizohanto.bakingapp.data.database.step;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StepDao {
    /**
     * @param stepId The id you want step for
     * @return {@link LiveData} of step with id specified
     */
    @Query("SELECT * FROM step WHERE recipeId = :recipeId AND id = :stepId")
    LiveData<Step> getStepById(int recipeId, int stepId);

    // Insert steps
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Step> steps);

    //
    @Query("DELETE FROM step")
    void deleteAllSteps();

}
