package com.zizohanto.bakingapp.ui.recipedetail;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.step.Step;
import com.zizohanto.bakingapp.data.utils.InjectorUtils;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ActDetailMaster}
 * in two-pane mode (on tablets) or a {@link ActDetailDetail}
 * on handsets.
 */
@SuppressWarnings("RedundantCast")
public class FragRecipeDetail extends Fragment {

    public static final String ARG_STEP_ID = "step_id";
    public static final String ARG_RECIPE_ID = "recipe_id";

    private int mRecipeId;
    private int mStepId;

    private TextView mStepShortDescription;

    private FragRecipeDetailViewModel mViewModel;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragRecipeDetail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP_ID) && getArguments().containsKey(ARG_RECIPE_ID)) {
            mStepId = getArguments().getInt(ARG_STEP_ID);
            mRecipeId = getArguments().getInt(ARG_RECIPE_ID);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(String.valueOf(mStepId));
            }

            setupViewModel();
            observeStep();
        }
    }

    private void setupViewModel() {
        FragRecipeDetailViewModelFactory factory = InjectorUtils.provideFRDViewModelFactory(getContext(),
                mRecipeId, mStepId);
        mViewModel = ViewModelProviders.of(this, factory).get(FragRecipeDetailViewModel.class);
    }

    private void observeStep() {
        mViewModel.getStep().observe(this, new Observer<Step>() {
            @Override
            public void onChanged(@Nullable Step step) {
                if (step != null) {
                    mStepShortDescription.setText(String.valueOf(step.getShortDescription()));
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);

        mStepShortDescription = (TextView) rootView.findViewById(R.id.recipe_step_detail);

        return rootView;
    }
}
