package com.milaap.app.home;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeRepository {

    private final FirebaseFirestore firestore;

    public HomeRepository() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void getPosts(
            @NonNull OnPostsLoadedListener listener
    ) {

        firestore.collection("posts")
                .orderBy(
                        "createdAt",
                        Query.Direction.DESCENDING
                )
                .get()
                .addOnSuccessListener(querySnapshot -> {

                    listener.onSuccess(querySnapshot);
                })
                .addOnFailureListener(listener::onFailure);
    }

    public interface OnPostsLoadedListener {

        void onSuccess(QuerySnapshot querySnapshot);

        void onFailure(Exception e);
    }
}