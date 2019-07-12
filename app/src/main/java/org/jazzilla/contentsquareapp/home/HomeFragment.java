package org.jazzilla.contentsquareapp.home;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jazzilla.contentsquareapp.BaseFragment;
import org.jazzilla.contentsquareapp.R;
import org.jazzilla.contentsquareapp.core.appmodel.RepositoryAppModel;
import org.jazzilla.contentsquareapp.core.provider.ProviderCallback;
import org.jazzilla.contentsquareapp.core.provider.RepositoryProvider;
import org.jazzilla.contentsquareapp.common.widget.EndlessRecyclerView;

import java.util.List;

public class HomeFragment extends BaseFragment {
    public static final String REPOSITORY_KEY = "repository";

    private EditText mSearchReposEditText;
    private EndlessRecyclerView mReposListRecyclerView;
    private ProgressBar mProgressBar;
    private LinearLayout mErrorView;
    private ImageView mErrorImageView;
    private TextView mErrorTextView;

    private boolean mIsLoading = false;
    private int mFromRepository = 0;

    private RepositoryAdapter mRepositoryAdapter = new RepositoryAdapter();

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void createView(View view) {
        mSearchReposEditText = view.findViewById(R.id.searchRepoEditText);
        mReposListRecyclerView = view.findViewById(R.id.reposListRecyclerView);
        mProgressBar = view.findViewById(R.id.progressBar);
        mErrorView = view.findViewById(R.id.errorLinearLayout);
        mErrorTextView = view.findViewById(R.id.errorTextView);
        mErrorImageView = view.findViewById(R.id.errorImageView);

        mRepositoryAdapter.setOnItemClickedListener(new RepositoryAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(RepositoryAppModel repository) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(REPOSITORY_KEY, repository);
                navigateTo(R.id.action_homeFragment_to_repositoryFragment, bundle);
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mReposListRecyclerView.setLayoutManager(layoutManager);
        mReposListRecyclerView.setHasFixedSize(false);
        mReposListRecyclerView.setAdapter(mRepositoryAdapter);

        mReposListRecyclerView.setEndlessListener(new EndlessRecyclerView.EndlessListener() {
            @Override
            public void onEndReach() {
                if(!mIsLoading) {
                    requestRepositoriesFromRepositoryId(mFromRepository);
                }
            }
        });

        mSearchReposEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String repositoryName = mSearchReposEditText.getText().toString();

                    if(repositoryName.isEmpty()) {
                        mFromRepository = 0;
                        requestAllRepositories();
                    } else {
                        searchRepositories(repositoryName);
                    }
                }

                return false;
            }
        });

        requestAllRepositories();
    }

    private void requestRepositoriesFromRepositoryId(int fromRepository) {
        RepositoryProvider.getAllPublicRepos(fromRepository, new ProviderCallback<List<RepositoryAppModel>>() {
            @Override
            public void start() {
                mIsLoading = true;
            }

            @Override
            public void finish() {
                mIsLoading = false;
            }

            @Override
            public void success(List<RepositoryAppModel> res) {
                mRepositoryAdapter.addItems(res);
                mFromRepository = Integer.parseInt(res.get(res.size() - 1).id);
            }

            @Override
            public void error(int error) { }
        });
    }

    private void requestAllRepositories() {
        RepositoryProvider.getAllPublicRepos(0, new ProviderCallback<List<RepositoryAppModel>>() {
            @Override
            public void start() {
                mProgressBar.setVisibility(View.VISIBLE);
                mReposListRecyclerView.setVisibility(View.GONE);
            }

            @Override
            public void finish() {
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void success(List<RepositoryAppModel> res) {
                mReposListRecyclerView.setVisibility(View.VISIBLE);
                mRepositoryAdapter.addItems(res);
                mFromRepository = Integer.parseInt(res.get(res.size() - 1).id);
            }

            @Override
            public void error(int error) {
                handleError(error);
            }
        });
    }

    private void searchRepositories(String repositoryName) {
        RepositoryProvider.searchRepositories(repositoryName, new ProviderCallback<List<RepositoryAppModel>>() {
            @Override
            public void start() {
                mReposListRecyclerView.setVisibility(View.GONE);
                mErrorView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void finish() {
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void success(List<RepositoryAppModel> res) {
                mReposListRecyclerView.setVisibility(View.VISIBLE);
                mRepositoryAdapter.setItems(res);

                if(res.size() == 0) {
                    mErrorView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void error(@RepositoryProvider.Error int error) {
                handleError(error);
            }
        });
    }

    private void handleError(@RepositoryProvider.Error int error) {
        @StringRes int errorText;
        @DrawableRes int errorDrawable;

        switch (error) {
            case RepositoryProvider.ERROR_NO_REPOSITORIES:
                errorText = R.string.home_errorNoRepository;
                errorDrawable = R.drawable.ic_no_content;
                break;
            case RepositoryProvider.ERROR_NETWORK:
                errorText = R.string.home_errorNoInternetConnection;
                errorDrawable = R.drawable.ic_signal_wifi_off;
                break;
            case RepositoryProvider.ERROR_GENERIC:
                errorText = R.string.home_error;
                errorDrawable = R.drawable.ic_error;
                break;
            default :
                errorText = R.string.home_error;
                errorDrawable = R.drawable.ic_error;
                break;
        }

        mErrorImageView.setImageResource(errorDrawable);
        mErrorTextView.setText(errorText);
        mReposListRecyclerView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }
}
