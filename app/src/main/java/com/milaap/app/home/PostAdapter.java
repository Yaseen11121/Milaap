package com.milaap.app.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.milaap.app.R;
import com.milaap.app.model.Post;

import java.util.List;

public class PostAdapter
        extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PostViewHolder holder,
            int position
    ) {

        Post post = postList.get(position);

        holder.tvUsername.setText(
                post.getUsername() != null
                        ? post.getUsername()
                        : "Milaap User"
        );

        holder.tvCaption.setText(
                post.getCaption() != null
                        ? post.getCaption()
                        : ""
        );

        holder.tvLikes.setText(
                post.getLikesCount() + " likes"
        );
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder
            extends RecyclerView.ViewHolder {

        ImageView ivPostProfile;
        ImageView ivPostImage;
        TextView tvUsername;
        TextView tvCaption;
        TextView tvLikes;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPostProfile =
                    itemView.findViewById(R.id.ivPostProfile);

            ivPostImage =
                    itemView.findViewById(R.id.ivPostImage);

            tvUsername =
                    itemView.findViewById(R.id.tvPostUsername);

            tvCaption =
                    itemView.findViewById(R.id.tvCaption);

            tvLikes =
                    itemView.findViewById(R.id.tvLikes);
        }
    }
}