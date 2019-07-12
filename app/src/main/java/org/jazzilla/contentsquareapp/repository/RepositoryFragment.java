package org.jazzilla.contentsquareapp.repository;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.jazzilla.contentsquareapp.BaseFragment;
import org.jazzilla.contentsquareapp.MainActivity;
import org.jazzilla.contentsquareapp.R;
import org.jazzilla.contentsquareapp.core.appmodel.RepositoryAppModel;
import org.jazzilla.contentsquareapp.core.provider.ProviderCallback;
import org.jazzilla.contentsquareapp.core.provider.RepositoryProvider;
import org.jazzilla.contentsquareapp.home.HomeFragment;
import org.jazzilla.contentsquareapp.repository.adapter.RepositoryPagerAdapter;
import org.jazzilla.contentsquareapp.repository.adapter.RepositoryTopicsAdapter;

import java.util.ArrayList;
import java.util.List;

public class RepositoryFragment extends BaseFragment {
    private ImageView mBackImageView;
    private TextView mRepositoryNameTextView;
    private RecyclerView mTopicsRecyclerView;
    private ImageView mOwnerImageView;
    private TextView mForkTextView;
    private RepositoryInfoItem mForkItem;
    private RepositoryInfoItem mStarItem;
    private RepositoryInfoItem mPushItem;
    private TextView mDescriptionTextView;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private RepositoryAppModel mRepository;


    @Override
    protected int getLayout() {
        return R.layout.fragment_repository;
    }

    @Override
    protected void createView(View view) {
        mBackImageView = view.findViewById(R.id.backImageView);
        mRepositoryNameTextView = view.findViewById(R.id.repositoryNameTextView);
        mForkTextView = view.findViewById(R.id.forkFromTextView);
        mDescriptionTextView = view.findViewById(R.id.repositoryDescriptionTextView);
        mTopicsRecyclerView = view.findViewById(R.id.topicsRecyclerView);
        mOwnerImageView = view.findViewById(R.id.ownerImageView);
        mForkItem = view.findViewById(R.id.repositoryInfoItemFork);
        mStarItem = view.findViewById(R.id.repositoryInfoItemStar);
        mPushItem = view.findViewById(R.id.repositoryInfoItemPush);
        mTabLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.viewPager);
        Bundle bundle = getArguments();

        if(bundle != null) {
            RepositoryAppModel repo = (RepositoryAppModel) bundle.getSerializable(HomeFragment.REPOSITORY_KEY);

            if(repo != null) {
                getRepository(repo.owner.name, repo.name);
            }
        }

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateBack();
            }
        });
    }

    private void getRepository(@NonNull String owner, @NonNull String repositoryName) {
        RepositoryProvider.getRepository(owner, repositoryName, new ProviderCallback<RepositoryAppModel>() {
            @Override
            public void start() { }

            @Override
            public void finish() { }

            @Override
            public void success(RepositoryAppModel res) {
                mRepository = res;
                bindView();
                getCommitCount();
            }

            @Override
            public void error(int error) {
                showSimpleError(R.string.home_error, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navigateBack();
                    }
                });
            }
        });
    }

    private void getCommitCount() {
        RepositoryProvider.getCommitCount(mRepository.owner.name, mRepository.name, mRepository.createdAt, new ProviderCallback<Integer>() {
            @Override
            public void start() { }

            @Override
            public void finish() { }

            @Override
            public void success(Integer res) {
                mPushItem.setText(Integer.toString(res));
            }

            @Override
            public void error(int error) {
                mPushItem.setText("0");
            }
        });
    }

    private void bindView() {
        if(mRepository.topics != null) {
            RepositoryTopicsAdapter adapter = new RepositoryTopicsAdapter(mRepository.topics);

            mTopicsRecyclerView.setHasFixedSize(true);
            mTopicsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            mTopicsRecyclerView.setAdapter(adapter);
        }

        if(mRepository.owner != null) {
            mRepositoryNameTextView.setText(mRepository.owner.name + "/" + mRepository.name);

            if(mRepository.owner.avatarUri != null) {
                Picasso.get()
                        .load(mRepository.owner.avatarUri)
                        .fit()
                        .placeholder(R.drawable.ic_person)
                        .into(mOwnerImageView);
            }
        }

        if(mRepository.isFork) {
            mForkTextView.setText(mRepository.parentName);
        }

        mForkItem.setText(Integer.toString(mRepository.forksCount));
        mStarItem.setText(Integer.toString(mRepository.starsCount));
        mPushItem.setText(Integer.toString(0));

        mDescriptionTextView.setText(mRepository.description);

        mViewPager.setOffscreenPageLimit(4);

        setupTabs();
    }

    private void setupTabs() {
        Context context = getContext();
        MainActivity activity = (MainActivity) getActivity();

        if(context != null && activity != null && mRepository != null && mRepository.owner != null) {
            String repository = mRepository.name;
            String owner = mRepository.owner.name;

            List<BaseFragment> fragmentList = new ArrayList<>();
            fragmentList.add(RepositoryListItemFragment.newInstance(owner, repository, RepositoryListItemFragment.MODE_BRANCH));
            fragmentList.add(RepositoryListItemFragment.newInstance(owner, repository, RepositoryListItemFragment.MODE_CONTRIBUTOR));
            fragmentList.add(RepositoryListItemFragment.newInstance(owner, repository, RepositoryListItemFragment.MODE_ISSUE));
            fragmentList.add(RepositoryListItemFragment.newInstance(owner, repository, RepositoryListItemFragment.MODE_PULL_REQUEST));

            RepositoryPagerAdapter adapter = new RepositoryPagerAdapter(fragmentList, context, getChildFragmentManager());

            mViewPager.setAdapter(adapter);
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }
}
