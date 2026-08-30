package com.milaap.app.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.milaap.app.R;
import com.milaap.app.auth.User;
import com.milaap.app.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding = FragmentProfileBinding.inflate(
                inflater,
                container,
                false
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        loadUserProfile();

        binding.tvLogout.setOnClickListener(v -> logout());
        binding.btnEditProfile.setOnClickListener(v -> {

            NavHostFragment.findNavController(this)
                    .navigate(
                            R.id.action_profileFragment_to_editProfileFragment
                    );
        });
    }

    private void loadUserProfile() {

        if (firebaseAuth.getCurrentUser() == null) {
            return;
        }

        String uid = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        User user = documentSnapshot.toObject(User.class);

                        if (user != null) {

                            binding.tvFullName.setText(
                                    user.getFullName()
                            );

                            binding.tvUsername.setText(
                                    "@" + user.getUsername()
                            );

                            binding.tvEmail.setText(
                                    user.getEmail()
                            );

                            if (user.getBio() != null
                                    && !user.getBio().isEmpty()) {

                                binding.tvBio.setText(
                                        user.getBio()
                                );

                            } else {

                                binding.tvBio.setText(
                                        "No bio yet"
                                );
                            }
                        }

                    } else {

                        Toast.makeText(
                                requireContext(),
                                "Profile not found",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            requireContext(),
                            "Failed to load profile",
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    private void logout() {

        FirebaseAuth.getInstance().signOut();

        NavController navController =
                NavHostFragment.findNavController(this);

        navController.navigate(
                R.id.loginFragment,
                null,
                new NavOptions.Builder()
                        .setPopUpTo(
                                R.id.homeFragment,
                                true
                        )
                        .build()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}