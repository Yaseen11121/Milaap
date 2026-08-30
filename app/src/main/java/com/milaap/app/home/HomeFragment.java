package com.milaap.app.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.milaap.app.R;
import com.milaap.app.databinding.FragmentHomeBinding;
import com.milaap.app.model.Post;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private PostAdapter postAdapter;
    private List<Post> postList;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding = FragmentHomeBinding.inflate(
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

        setupRecyclerView();
        loadDummyPosts();
    }

    private void setupRecyclerView() {

        postList = new ArrayList<>();

        postAdapter = new PostAdapter(postList);

        binding.recyclerViewPosts.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        binding.recyclerViewPosts.setAdapter(postAdapter);

        binding.recyclerViewPosts.setNestedScrollingEnabled(false);
    }

    private void loadDummyPosts() {

        Post post1 = new Post(
                "post1",
                "user1",
                "milaap_user",
                "",
                "",
                "Welcome to Milaap! 🚀",
                120,
                10,
                System.currentTimeMillis()
        );

        Post post2 = new Post(
                "post2",
                "user2",
                "yaseen_dev",
                "",
                "",
                "Building something amazing with Milaap.",
                85,
                7,
                System.currentTimeMillis()
        );

        Post post3 = new Post(
                "post3",
                "user3",
                "milaap_creator",
                "",
                "",
                "This is our first social feed! ❤️",
                250,
                24,
                System.currentTimeMillis()
        );

        postList.add(post1);
        postList.add(post2);
        postList.add(post3);

        postAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}