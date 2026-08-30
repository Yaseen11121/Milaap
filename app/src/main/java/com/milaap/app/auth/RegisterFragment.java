package com.milaap.app.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.milaap.app.R;
import com.milaap.app.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentRegisterBinding.inflate(
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

        authViewModel = new ViewModelProvider(this)
                .get(AuthViewModel.class);

        setupClickListeners();
        observeAuthState();
    }

    // --------------------------------------------------
    // CLICK LISTENERS
    // --------------------------------------------------

    private void setupClickListeners() {

        binding.btnRegister.setOnClickListener(v -> register());

        binding.tvLogin.setOnClickListener(v -> {

            NavHostFragment.findNavController(this)
                    .navigateUp();
        });
    }

    // --------------------------------------------------
    // REGISTRATION
    // --------------------------------------------------

    private void register() {

        String fullName = getText(binding.etName);
        String username = getText(binding.etUsername);
        String email = getText(binding.etEmail);
        String password = getText(binding.etPassword);
        String confirmPassword = getText(binding.etConfirmPassword);

        // Clear previous errors
        binding.tilName.setError(null);
        binding.tilUsername.setError(null);
        binding.tilEmail.setError(null);
        binding.tilPassword.setError(null);
        binding.tilConfirmPassword.setError(null);

        // Full Name validation
        if (TextUtils.isEmpty(fullName)) {

            binding.tilName.setError("Full name is required");
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

        username = username.toLowerCase().trim();

        if (!username.matches("^[a-z0-9_]{3,20}$")) {

            binding.tilUsername.setError(
                    "Use 3-20 characters: letters, numbers or _"
            );
            binding.etUsername.requestFocus();
            return;
        }

        // Email validation
        if (TextUtils.isEmpty(email)) {

            binding.tilEmail.setError(
                    "Email is required"
            );
            binding.etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            binding.tilEmail.setError(
                    "Enter a valid email"
            );
            binding.etEmail.requestFocus();
            return;
        }

        // Password validation
        if (TextUtils.isEmpty(password)) {

            binding.tilPassword.setError(
                    "Password is required"
            );
            binding.etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {

            binding.tilPassword.setError(
                    "Password must be at least 6 characters"
            );
            binding.etPassword.requestFocus();
            return;
        }

        // Confirm password validation
        if (TextUtils.isEmpty(confirmPassword)) {

            binding.tilConfirmPassword.setError(
                    "Please confirm your password"
            );
            binding.etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {

            binding.tilConfirmPassword.setError(
                    "Passwords do not match"
            );
            binding.etConfirmPassword.requestFocus();
            return;
        }

        // Create User object
        User user = new User(
                "",
                username,
                fullName,
                email,
                "",
                ""
        );

        // Send registration request
        authViewModel.register(user, password);
    }

    // --------------------------------------------------
    // OBSERVE AUTH STATE
    // --------------------------------------------------

    private void observeAuthState() {

        authViewModel.getLoading().observe(
                getViewLifecycleOwner(),
                isLoading -> {

                    binding.btnRegister.setEnabled(!isLoading);

                    if (isLoading) {

                        binding.btnRegister.setText(
                                "Creating account..."
                        );

                    } else {

                        binding.btnRegister.setText(
                                "Create Account"
                        );
                    }
                }
        );

        authViewModel.getError().observe(
                getViewLifecycleOwner(),
                error -> {

                    if (error != null && !error.isEmpty()) {

                        Toast.makeText(
                                requireContext(),
                                error,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );

        authViewModel.getRegisterSuccess().observe(
                getViewLifecycleOwner(),
                success -> {

                    if (Boolean.TRUE.equals(success)) {

                        Toast.makeText(
                                requireContext(),
                                "Account created successfully!",
                                Toast.LENGTH_SHORT
                        ).show();

                        NavOptions navOptions =
                                new NavOptions.Builder()
                                        .setPopUpTo(
                                                R.id.registerFragment,
                                                true
                                        )
                                        .build();

                        NavHostFragment.findNavController(this)
                                .navigate(
                                        R.id.homeFragment,
                                        null,
                                        navOptions
                                );
                    }
                }
        );
    }

    // --------------------------------------------------
    // HELPER
    // --------------------------------------------------

    private String getText(
            com.google.android.material.textfield.TextInputEditText editText
    ) {

        if (editText.getText() == null) {
            return "";
        }

        return editText.getText()
                .toString()
                .trim();
    }

    // --------------------------------------------------
    // CLEANUP
    // --------------------------------------------------

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}