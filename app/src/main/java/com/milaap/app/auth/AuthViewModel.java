package com.milaap.app.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AuthViewModel extends ViewModel {

    private final AuthRepository repository;

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();

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
                    error.setValue(e.getMessage());
                });
    }

    public void register(String email, String password) {
        loading.setValue(true);
        error.setValue(null);

        repository.register(email, password)
                .addOnSuccessListener(result -> {
                    loading.setValue(false);
                    registerSuccess.setValue(true);
                })
                .addOnFailureListener(e -> {
                    loading.setValue(false);
                    error.setValue(e.getMessage());
                });
    }

    public void sendPasswordResetEmail(String email) {
        loading.setValue(true);
        error.setValue(null);

        repository.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> {
                    loading.setValue(false);
                })
                .addOnFailureListener(e -> {
                    loading.setValue(false);
                    error.setValue(e.getMessage());
                });
    }
}