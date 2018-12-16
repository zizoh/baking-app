package com.zizohanto.bakingapp.ui.recipedetail;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zizohanto.bakingapp.R;
import com.zizohanto.bakingapp.data.database.step.Step;

import java.util.List;

@SuppressWarnings("RedundantCast")
public class RecipeStepDescriptionAdapter
        extends RecyclerView.Adapter<RecipeStepDescriptionAdapter.ViewHolder> {

    private int mSelectedPos = RecyclerView.NO_POSITION;
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
                .inflate(R.layout.step_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Step step = mRecipeSteps.get(position);
        holder.tvShortStepDescription.setText(step.getShortDescription());
        if (stepHasVideo(step)) {
            holder.ivVideoIcon.setVisibility(View.VISIBLE);
        }
        holder.itemView.setSelected(mSelectedPos == position);
    }

    private boolean stepHasVideo(Step step) {
        return !TextUtils.isEmpty(step.getVideoURL());
    }

    @Override
    public int getItemCount() {
        if (mRecipeSteps == null) return 0;
        return mRecipeSteps.size();
    }

    public interface StepClickListener {
        void onStepClick(Step clickedStep, int clickedStepPosition);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvShortStepDescription;
        private ImageView ivVideoIcon;

        ViewHolder(View view) {
            super(view);
            tvShortStepDescription = (TextView) view.findViewById(R.id.tv_step_short_desc);
            ivVideoIcon = (ImageView) view.findViewById(R.id.iv_step_video_icon);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            notifyItemChanged(mSelectedPos);
            mSelectedPos = getLayoutPosition();
            notifyItemChanged(mSelectedPos);

            int clickedStepPosition = getAdapterPosition();
            mOnClickListener.onStepClick(mRecipeSteps.get(clickedStepPosition), clickedStepPosition);
        }
    }
}