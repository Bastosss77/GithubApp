package org.jazzilla.contentsquareapp;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);

        createView(view);

        return view;
    }

    abstract protected @LayoutRes int getLayout();
    abstract protected void createView(View view);

    protected void navigateTo(@IdRes int destination, @Nullable Bundle bundle) {
        MainActivity mainActivity = (MainActivity) getActivity();

        if(mainActivity != null) {
            mainActivity.navigateTo(destination, bundle);
        }
    }

    protected void navigateBack() {
        MainActivity mainActivity = (MainActivity) getActivity();

        if(mainActivity != null) {
            mainActivity.navigateBack();
        }
    }

    protected void showSimpleError(@StringRes int errorMessage, DialogInterface.OnClickListener okListener) {
        Context context = getContext();

        if(context != null) {
            new AlertDialog.Builder(getContext())
                    .setMessage(errorMessage)
                    .setNegativeButton(android.R.string.ok, okListener)
                    .create()
                    .show();
        }
    }

    public @StringRes int getTitle() {
        return 0;
    }
}
