package com.milaap.app.model;

public class Post {

    private String postId;
    private String userId;
    private String username;
    private String profileImageUrl;
    private String imageUrl;
    private String caption;
    private long likesCount;
    private long commentsCount;
    private long createdAt;

    public Post() {
        // Required empty constructor for Firestore
    }

    public Post(
            String postId,
            String userId,
            String username,
            String profileImageUrl,
            String imageUrl,
            String caption,
            long likesCount,
            long commentsCount,
            long createdAt
    ) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.createdAt = createdAt;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}