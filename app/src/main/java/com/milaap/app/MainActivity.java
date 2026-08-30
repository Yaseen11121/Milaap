package com.milaap.app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.milaap.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) {
            return;
        }

        NavController navController =
                navHostFragment.getNavController();

        BottomNavigationView bottomNavigationView =
                binding.bottomNavigationView;

        NavigationUI.setupWithNavController(
                bottomNavigationView,
                navController
        );

        // Show/hide bottom navigation
        navController.addOnDestinationChangedListener(
                (controller, destination, arguments) -> {

                    int destinationId = destination.getId();

                    if (destinationId == R.id.loginFragment ||
                            destinationId == R.id.registerFragment ||
                            destinationId == R.id.editProfileFragment) {

                        bottomNavigationView.setVisibility(View.GONE);

                    } else {

                        bottomNavigationView.setVisibility(View.VISIBLE);
                    }
                }
        );

        checkAuthentication(navController);
    }

    private void checkAuthentication(NavController navController) {

        if (firebaseAuth.getCurrentUser() != null) {

            navController.navigate(
                    R.id.homeFragment,
                    null,
                    new androidx.navigation.NavOptions.Builder()
                            .setPopUpTo(
                                    R.id.loginFragment,
                                    true
                            )
                            .build()
            );

        }
    }
}