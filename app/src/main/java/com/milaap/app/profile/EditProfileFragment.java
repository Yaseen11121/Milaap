package com.milaap.app.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.milaap.app.databinding.FragmentEditProfileBinding;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private String currentUid;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding = FragmentEditProfileBinding.inflate(
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

        if (firebaseAuth.getCurrentUser() == null) {
            return;
        }

        currentUid = firebaseAuth.getCurrentUser().getUid();

        loadProfile();

        binding.btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadProfile() {

        firestore.collection("users")
                .document(currentUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (!documentSnapshot.exists()) {
                        Toast.makeText(
                                requireContext(),
                                "Profile not found",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    String fullName =
                            documentSnapshot.getString("fullName");

                    String username =
                            documentSnapshot.getString("username");

                    String bio =
                            documentSnapshot.getString("bio");

                    binding.etName.setText(
                            fullName != null ? fullName : ""
                    );

                    binding.etUsername.setText(
                            username != null ? username : ""
                    );

                    binding.etBio.setText(
                            bio != null ? bio : ""
                    );
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            requireContext(),
                            "Failed to load profile",
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    private void saveProfile() {

        String fullName = binding.etName
                .getText()
                .toString()
                .trim();

        String username = binding.etUsername
                .getText()
                .toString()
                .trim()
                .toLowerCase();

        String bio = binding.etBio
                .getText()
                .toString()
                .trim();

        // Clear old errors
        binding.tilName.setError(null);
        binding.tilUsername.setError(null);
        binding.tilBio.setError(null);

        // Full name validation
        if (TextUtils.isEmpty(fullName)) {

            binding.tilName.setError(
                    "Full name is required"
            );

            binding.etName.requestFocus();
            return;
        }

        if (fullName.length() < 2) {

            binding.tilName.setError(
                    "Name must contain at least 2 characters"
            );

            binding.etName.requestFocus();
            return;
        }

        // Username validation
        if (TextUtils.isEmpty(username)) {

            binding.tilUsername.setError(
                    "Username is required"
            );

            binding.etUsername.requestFocus();
            return;
        }

        if (!username.matches("^[a-z0-9_]{3,20}$")) {

            binding.tilUsername.setError(
                    "Use 3-20 characters: letters, numbers or _"
            );

            binding.etUsername.requestFocus();
            return;
        }

        // Disable button while saving
        binding.btnSave.setEnabled(false);
        binding.btnSave.setText("Saving...");

        // Only update editable fields
        java.util.Map<String, Object> updates =
                new java.util.HashMap<>();

        updates.put("fullName", fullName);
        updates.put("username", username);
        updates.put("bio", bio);

        firestore.collection("users")
                .document(currentUid)
                .update(updates)
                .addOnSuccessListener(unused -> {

                    binding.btnSave.setEnabled(true);
                    binding.btnSave.setText("Save Changes");

                    Toast.makeText(
                            requireContext(),
                            "Profile updated successfully!",
                            Toast.LENGTH_SHORT
                    ).show();

                    // Go back to Profile
                    requireActivity()
                            .getOnBackPressedDispatcher()
                            .onBackPressed();
                })
                .addOnFailureListener(e -> {

                    binding.btnSave.setEnabled(true);
                    binding.btnSave.setText("Save Changes");

                    Toast.makeText(
                            requireContext(),
                            "Failed to update profile",
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}