package org.jazzilla.contentsquareapp.repository.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jazzilla.contentsquareapp.R;
import org.jazzilla.contentsquareapp.core.appmodel.BaseAppModel;
import org.jazzilla.contentsquareapp.core.appmodel.IssueAppModel;
import org.jazzilla.contentsquareapp.core.appmodel.PullRequestAppModel;
import org.jazzilla.contentsquareapp.core.appmodel.RepositoryBranchAppModel;
import org.jazzilla.contentsquareapp.core.appmodel.UserAppModel;
import org.jazzilla.contentsquareapp.repository.RepositoryListItemFragment;

import java.util.ArrayList;
import java.util.List;

import static org.jazzilla.contentsquareapp.repository.RepositoryListItemFragment.MODE_BRANCH;
import static org.jazzilla.contentsquareapp.repository.RepositoryListItemFragment.MODE_CONTRIBUTOR;
import static org.jazzilla.contentsquareapp.repository.RepositoryListItemFragment.MODE_ISSUE;
import static org.jazzilla.contentsquareapp.repository.RepositoryListItemFragment.MODE_PULL_REQUEST;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {
    private List<BaseAppModel> mItems = new ArrayList<>();
    private @RepositoryListItemFragment.Mode int mMode;

    public ListItemAdapter(@RepositoryListItemFragment.Mode int mode) {
        mMode = mode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = null;

        switch (mMode) {
            case MODE_BRANCH: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repository_branch_list_item, parent, false);
                viewHolder = new BranchViewHolder(v);
                break;
            }

            case MODE_CONTRIBUTOR: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repository_contributor_list_item, parent, false);
                viewHolder = new ContributorViewHolder(v);
                break;
            }

            case MODE_ISSUE: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repository_issue_list_item, parent, false);
                viewHolder = new IssueViewHolder(v);
                break;
            }

            case MODE_PULL_REQUEST: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.repository_pull_request_list_item, parent, false);
                viewHolder = new PullRequestViewHolder(v);
                break;
            }
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItems(List<? extends BaseAppModel> models) {
        mItems.addAll(models);
        notifyDataSetChanged();
    }

    abstract class ViewHolder<T extends BaseAppModel> extends RecyclerView.ViewHolder {

        ViewHolder(View v) {
            super(v);
        }

        public abstract void bind(T model);
    }

    final class BranchViewHolder extends ViewHolder<RepositoryBranchAppModel> {
        private TextView mBranchNameTextView;

        BranchViewHolder(View v) {
            super(v);

            mBranchNameTextView = v.findViewById(R.id.branchNameTextView);
        }

        @Override
        public void bind(RepositoryBranchAppModel model) {
            mBranchNameTextView.setText(model.name);
        }
    }

    final class ContributorViewHolder extends ViewHolder<UserAppModel> {
        private TextView mContributorNameTextVIew;

        ContributorViewHolder(View view) {
            super(view);

            mContributorNameTextVIew = view.findViewById(R.id.contributorNameTextView);
        }

        @Override
        public void bind(UserAppModel user) {
            mContributorNameTextVIew.setText(user.name);
        }
    }

    final class IssueViewHolder extends ViewHolder<IssueAppModel> {
        private TextView mIssueNameTextVIew;
        private ImageView mIssueImageView;

        IssueViewHolder(View view) {
            super(view);

            mIssueNameTextVIew = view.findViewById(R.id.issueNameTextView);
            mIssueImageView = view.findViewById(R.id.issueImageView);
        }

        @Override
        public void bind(IssueAppModel issue) {
            @DrawableRes int drawable = issue.state == IssueAppModel.State.OPEN ? R.drawable.ic_lock_open : R.drawable.ic_lock_close;

            mIssueNameTextVIew.setText(issue.title);
            mIssueImageView.setImageResource(drawable);
        }
    }

    final class PullRequestViewHolder extends ViewHolder<PullRequestAppModel> {
        private TextView mPullRequestNameTextView;

        PullRequestViewHolder(View view) {
            super(view);

            mPullRequestNameTextView = view.findViewById(R.id.pullRequestNameTextView);
        }

        @Override
        public void bind(PullRequestAppModel pr) {
            mPullRequestNameTextView.setText(pr.title);
        }
    }
}
