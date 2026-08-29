package com.milaap.app.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.milaap.app.R;
import com.milaap.app.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
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

    private void setupClickListeners() {

        binding.btnLogin.setOnClickListener(v -> login());

        binding.tvCreateAccount.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_loginFragment_to_registerFragment);
        });

        binding.tvForgotPassword.setOnClickListener(v -> {
            String email = binding.etEmail.getText() != null
                    ? binding.etEmail.getText().toString().trim()
                    : "";

            if (TextUtils.isEmpty(email)) {
                binding.tilEmail.setError("Enter your email first");
                return;
            }

            binding.tilEmail.setError(null);

            authViewModel.sendPasswordResetEmail(email);
        });
    }

    private void login() {

        String email = binding.etEmail.getText() != null
                ? binding.etEmail.getText().toString().trim()
                : "";

        String password = binding.etPassword.getText() != null
                ? binding.etPassword.getText().toString()
                : "";

        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError("Email is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Enter a valid email");
            return;
        }

        binding.tilEmail.setError(null);

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("Password is required");
            return;
        }

        binding.tilPassword.setError(null);

        authViewModel.login(email, password);
    }

    private void observeAuthState() {

        authViewModel.getLoading().observe(
                getViewLifecycleOwner(),
                isLoading -> {
                    binding.btnLogin.setEnabled(!isLoading);

                    if (isLoading) {
                        binding.btnLogin.setText("Logging in...");
                    } else {
                        binding.btnLogin.setText("Login");
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

        authViewModel.getLoginSuccess().observe(
                getViewLifecycleOwner(),
                success -> {
                    if (Boolean.TRUE.equals(success)) {
                        Toast.makeText(
                                requireContext(),
                                "Login successful!",
                                Toast.LENGTH_SHORT
                        ).show();

                        // Home navigation will be added next.
                    }
                }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}