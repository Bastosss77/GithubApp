package org.jazzilla.contentsquareapp;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
    }

    public void navigateTo(@IdRes int destination, @Nullable Bundle bundle) {
        mNavController.navigate(destination, bundle);
    }

    public void navigateBack() {
        mNavController.popBackStack();
    }
}
