package org.jazzilla.contentsquareapp.repository.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jazzilla.contentsquareapp.BaseFragment;

import java.util.List;


public class RepositoryPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<BaseFragment> mFragments;

    public RepositoryPagerAdapter(@NonNull List<BaseFragment> fragments, @NonNull Context context, @NonNull FragmentManager fragmentManager) {
        super(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        @StringRes int titleRes = mFragments.get(position).getTitle();

        return titleRes == 0 ? null : mContext.getString(titleRes);
    }
}
