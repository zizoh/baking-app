package com.zizohanto.bakingapp.ui.recipedetail;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.step.Step;

import java.util.List;

@SuppressWarnings("RedundantCast")
public class RecipeStepDescriptionAdapter
        extends RecyclerView.Adapter<RecipeStepDescriptionAdapter.ViewHolder> {

    private StepClickListener mOnClickListener;
    private List<Step> mRecipeSteps;

    RecipeStepDescriptionAdapter(StepClickListener listener) {
        mOnClickListener = listener;
    }

    void setStepsData(List<Step> newSteps) {
        // If there was no step data, then recreate all of the list
        if (mRecipeSteps == null) {
            mRecipeSteps = newSteps;
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mRecipeSteps.size();
                }

                @Override
                public int getNewListSize() {
                    return newSteps.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mRecipeSteps.get(oldItemPosition).getId() ==
                            newSteps.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Step newStep = newSteps.get(newItemPosition);
                    Step oldStep = mRecipeSteps.get(oldItemPosition);
                    return newStep.getId() == oldStep.getId()
                            && newStep.getShortDescription().equals(oldStep.getShortDescription());
                }
            });
            mRecipeSteps = newSteps;
            result.dispatchUpdatesTo(this);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_master_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mIdView.setText(mRecipeSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mRecipeSteps == null) return 0;
        return mRecipeSteps.size();
    }

    public interface StepClickListener {
        void onStepClick(Step clickedStep);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mIdView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.tv_step_short_desc);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onStepClick(mRecipeSteps.get(clickedPosition));
        }
    }
}