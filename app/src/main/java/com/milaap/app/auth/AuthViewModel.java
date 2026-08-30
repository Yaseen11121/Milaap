package com.milaap.app.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AuthViewModel extends ViewModel {

    private final AuthRepository repository;

    private final MutableLiveData<Boolean> loading =
            new MutableLiveData<>(false);

    private final MutableLiveData<String> error =
            new MutableLiveData<>();

    private final MutableLiveData<Boolean> loginSuccess =
            new MutableLiveData<>();

    private final MutableLiveData<Boolean> registerSuccess =
            new MutableLiveData<>();

    public AuthViewModel() {
        repository = new AuthRepository();
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<Boolean> getRegisterSuccess() {
        return registerSuccess;
    }

    // ---------------- LOGIN ----------------

    public void login(String email, String password) {

        loading.setValue(true);
        error.setValue(null);

        repository.login(email, password)
                .addOnSuccessListener(result -> {

                    loading.setValue(false);
                    loginSuccess.setValue(true);

                })
                .addOnFailureListener(e -> {

                    loading.setValue(false);
                    error.setValue(getFirebaseErrorMessage(e));

                });
    }

    // ---------------- REGISTER ----------------

    public void register(User user, String password) {

        loading.setValue(true);
        error.setValue(null);
        registerSuccess.setValue(false);

        repository.register(user.getEmail(), password)
                .addOnSuccessListener(result -> {

                    String uid = result.getUser().getUid();

                    user.setUid(uid);

                    repository.saveUser(user)
                            .addOnSuccessListener(unused -> {

                                loading.setValue(false);
                                registerSuccess.setValue(true);

                            })
                            .addOnFailureListener(e -> {

                                loading.setValue(false);
                                error.setValue(
                                        "Account created, but profile could not be saved."
                                );

                            });

                })
                .addOnFailureListener(e -> {

                    loading.setValue(false);
                    error.setValue(getFirebaseErrorMessage(e));

                });
    }

    // ---------------- FORGOT PASSWORD ----------------

    public void sendPasswordResetEmail(String email) {

        loading.setValue(true);
        error.setValue(null);

        repository.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> {

                    loading.setValue(false);

                })
                .addOnFailureListener(e -> {

                    loading.setValue(false);
                    error.setValue(getFirebaseErrorMessage(e));

                });
    }

    // ---------------- FIREBASE ERROR HANDLING ----------------

    private String getFirebaseErrorMessage(Exception e) {

        String message = e.getMessage();

        if (message == null || message.isEmpty()) {
            return "Something went wrong. Please try again.";
        }

        if (message.contains("The email address is already in use")) {
            return "This email is already registered.";
        }

        if (message.contains("The email address is badly formatted")) {
            return "Please enter a valid email address.";
        }

        if (message.contains("The password is invalid")) {
            return "Incorrect password.";
        }

        if (message.contains("There is no user record")) {
            return "No account found with this email.";
        }

        if (message.contains("network error")) {
            return "Network error. Please check your internet connection.";
        }

        return message;
    }
}