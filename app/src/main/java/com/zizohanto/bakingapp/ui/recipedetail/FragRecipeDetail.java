package com.zizohanto.bakingapp.ui.recipedetail;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.step.Step;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link ActDetailMaster}
 * in two-pane mode (on tablets) or a {@link ActDetailDetail}
 * on handsets.
 */
@SuppressWarnings("RedundantCast")
public class FragRecipeDetail extends Fragment {

    public static final String ARG_STEP = "com.zizohanto.bakingapp.ui.recipedetail.ARG_STEP";

    private Step mStep;

    private TextView mStepShortDescription;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragRecipeDetail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP)) {
            mStep = getArguments().getParcelable(ARG_STEP);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null && mStep != null) {
                appBarLayout.setTitle(String.valueOf(mStep.getId()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);

        mStepShortDescription = (TextView) rootView.findViewById(R.id.recipe_step_detail);

        stepStepData(mStep);

        return rootView;
    }

    private void stepStepData(Step step) {
        mStepShortDescription.setText(String.valueOf(step.getShortDescription()));
    }
}
