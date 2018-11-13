package com.zizohanto.bakingapp.ui.recipedetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.zizohanto.bakingapp.data.BakingAppRepository;
import com.zizohanto.bakingapp.data.database.step.Step;

public class FragRecipeDetailViewModel extends ViewModel {
    private final int mRecipeId;
    private final int mStepId;
    private final BakingAppRepository mRepository;
    private LiveData<Step> mStep;

    FragRecipeDetailViewModel(BakingAppRepository repository, int recipeId, int stepId) {
        mRecipeId = recipeId;
        mStepId = stepId;
        mRepository = repository;
        mStep = mRepository.getStepById(mRecipeId, stepId);
    }

    public LiveData<Step> getStep() {
        return mStep;
    }
}
