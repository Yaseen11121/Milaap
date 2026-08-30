package com.milaap.app.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.milaap.app.model.Post;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final HomeRepository repository;

    private final MutableLiveData<List<Post>> posts =
            new MutableLiveData<>(new ArrayList<>());

    private final MutableLiveData<String> error =
            new MutableLiveData<>();

    private final MutableLiveData<Boolean> loading =
            new MutableLiveData<>(false);

    public HomeViewModel() {
        repository = new HomeRepository();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void loadPosts() {

        loading.setValue(true);
        error.setValue(null);

        repository.getPosts(
                new HomeRepository.OnPostsLoadedListener() {

                    @Override
                    public void onSuccess(
                            com.google.firebase.firestore.QuerySnapshot querySnapshot
                    ) {

                        List<Post> postList =
                                querySnapshot.toObjects(Post.class);

                        posts.setValue(postList);
                        loading.setValue(false);
                    }

                    @Override
                    public void onFailure(Exception e) {

                        loading.setValue(false);

                        error.setValue(
                                e.getMessage() != null
                                        ? e.getMessage()
                                        : "Failed to load posts"
                        );
                    }
                }
        );
    }
}