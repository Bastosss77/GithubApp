package org.jazzilla.contentsquareapp.repository;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import org.jazzilla.contentsquareapp.BaseFragment;
import org.jazzilla.contentsquareapp.R;
import org.jazzilla.contentsquareapp.core.appmodel.IssueAppModel;
import org.jazzilla.contentsquareapp.core.appmodel.PullRequestAppModel;
import org.jazzilla.contentsquareapp.core.appmodel.RepositoryBranchAppModel;
import org.jazzilla.contentsquareapp.core.appmodel.UserAppModel;
import org.jazzilla.contentsquareapp.core.provider.ProviderCallback;
import org.jazzilla.contentsquareapp.core.provider.RepositoryProvider;

import org.jazzilla.contentsquareapp.repository.adapter.ListItemAdapter;
import org.jazzilla.contentsquareapp.common.widget.EndlessRecyclerView;

import java.util.List;

public class RepositoryListItemFragment extends BaseFragment {
    private EndlessRecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private @Mode int mMode;
    private ListItemAdapter mAdapter;

    private int mPage = 1;
    private boolean mIsLoading = false;
    private boolean mCanLoadMore = true;

    private static final String OWNER_NAME_KEY = "owner";
    private static final String REPOSITORY_NAME_KEY = "repositoryName";

    public static final int MODE_BRANCH = 0;
    public static final int MODE_CONTRIBUTOR = 1;
    public static final int MODE_ISSUE = 2;
    public static final int MODE_PULL_REQUEST = 3;

    private static final int DEFAULT_RESULT_COUNT_PER_PAGE = 100;

    @IntDef({MODE_BRANCH, MODE_CONTRIBUTOR, MODE_ISSUE, MODE_PULL_REQUEST})
    public @interface Mode {}

    private RepositoryListItemFragment(@Mode int mode) {
        mMode = mode;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_repository_tab;
    }

    @Override
    protected void createView(View view) {
        Bundle bundle = getArguments();

        if(bundle != null) {
            mProgressBar = view.findViewById(R.id.repositoryListItemProgressBar);
            mRecyclerView = view.findViewById(R.id.recyclerView);
            mAdapter = new ListItemAdapter(mMode);

            mRecyclerView.setHasFixedSize(false);
            mRecyclerView.setAdapter(mAdapter);

            final String owner = bundle.getString(OWNER_NAME_KEY);
            final String repository = bundle.getString(REPOSITORY_NAME_KEY);

            if(owner == null || repository == null) {
                return;
            }

            mRecyclerView.setEndlessListener(new EndlessRecyclerView.EndlessListener() {
                @Override
                public void onEndReach() {
                    if(!mIsLoading && mCanLoadMore) {
                        makeRequest(owner, repository);
                    }
                }
            });

            makeRequest(owner, repository);
        }
    }

    @Override
    public @StringRes int getTitle() {
        switch (mMode) {
            case MODE_BRANCH:
                return R.string.repository_branchesTabTitle;
            case MODE_CONTRIBUTOR:
                return R.string.repository_contributorsTabTitle;
            case MODE_ISSUE:
                return R.string.repository_issuesTabTitle;
            case MODE_PULL_REQUEST:
                return R.string.repository_pullRequestTabTitle;
            default:
                return 0;
        }
    }

    private void makeRequest(String owner, String repository) {
        mIsLoading = true;

        switch (mMode) {
            case MODE_BRANCH:
                requestBranches(owner, repository);
                break;
            case MODE_CONTRIBUTOR:
                requestContributors(owner, repository);
                break;
            case MODE_ISSUE:
                requestIssues(owner, repository);
                break;
            case MODE_PULL_REQUEST:
                requestPullRequests(owner, repository);
                break;
        }
    }

    private void handleResultCount(int count) {
        if(count < DEFAULT_RESULT_COUNT_PER_PAGE) {
            mCanLoadMore = false;
        } else {
            mPage++;
        }
    }

    private void requestBranches(@NonNull String owner, @NonNull String repository) {
        RepositoryProvider.getBranches(mPage, DEFAULT_RESULT_COUNT_PER_PAGE, owner, repository, new ProviderCallback<List<RepositoryBranchAppModel>>() {
            @Override
            public void start() {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void finish() {
                mProgressBar.setVisibility(View.GONE);
                mIsLoading = false;
            }

            @Override
            public void success(List<RepositoryBranchAppModel> res) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.addItems(res);
                handleResultCount(res.size());
            }

            @Override
            public void error(int error) { }
        });
    }

    private void requestContributors(@NonNull String owner, @NonNull String repository) {
        RepositoryProvider.getContributors(mPage, DEFAULT_RESULT_COUNT_PER_PAGE, owner, repository, new ProviderCallback<List<UserAppModel>>() {
            @Override
            public void start() {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void finish() {
                mProgressBar.setVisibility(View.GONE);
                mIsLoading = false;
            }

            @Override
            public void success(List<UserAppModel> res) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.addItems(res);
                handleResultCount(res.size());
            }

            @Override
            public void error(int error) { }
        });
    }

    private void requestIssues(@NonNull String owner, @NonNull String repository) {
        RepositoryProvider.getIssues(mPage, DEFAULT_RESULT_COUNT_PER_PAGE, owner, repository, new ProviderCallback<List<IssueAppModel>>() {
            @Override
            public void start() {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void finish() {
                mProgressBar.setVisibility(View.GONE);
                mIsLoading = false;
            }

            @Override
            public void success(List<IssueAppModel> res) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.addItems(res);
                handleResultCount(res.size());
            }

            @Override
            public void error(int error) { }
        });
    }

    private void requestPullRequests(@NonNull String owner, @NonNull String repository) {
        RepositoryProvider.getOpenPullRequests(mPage, DEFAULT_RESULT_COUNT_PER_PAGE, owner, repository, new ProviderCallback<List<PullRequestAppModel>>() {
            @Override
            public void start() {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void finish() {
                mProgressBar.setVisibility(View.GONE);
                mIsLoading = false;
            }

            @Override
            public void success(List<PullRequestAppModel> res) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mAdapter.addItems(res);
                handleResultCount(res.size());
            }

            @Override
            public void error(int error) { }
        });
    }

    public static RepositoryListItemFragment newInstance(@NonNull String repositoryOwner, @NonNull String repositoryName, @Mode int mode) {
        Bundle bundle = new Bundle();
        RepositoryListItemFragment fragment = new RepositoryListItemFragment(mode);

        bundle.putString(OWNER_NAME_KEY, repositoryOwner);
        bundle.putString(REPOSITORY_NAME_KEY, repositoryName);

        fragment.setArguments(bundle);

        return fragment;
    }
}
