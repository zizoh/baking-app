package com.zizohanto.bakingapp.ui.recipedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

    private final ActDetailMaster mParentActivity;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Step step = (Step) view.getTag();

            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(FragRecipeDetail.ARG_STEP, step);

                FragRecipeDetail fragment = new FragRecipeDetail();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, ActDetailDetail.class);
                intent.putExtra(FragRecipeDetail.ARG_STEP, step);

                context.startActivity(intent);
            }
        }
    };
    private List<Step> mRecipeSteps;

    RecipeStepDescriptionAdapter(ActDetailMaster parent, boolean twoPane) {
        mParentActivity = parent;
        mTwoPane = twoPane;
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

        holder.itemView.setTag(mRecipeSteps.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        if (mRecipeSteps == null) return 0;
        return mRecipeSteps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.tv_step_short_desc);
        }
    }
}
