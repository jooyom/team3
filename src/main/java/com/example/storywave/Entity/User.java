package com.example.storywave.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String nickname;
    private String email;
    private String role;
    @Column(name = "active_status")
    private String activeStatus;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Column
    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @Column
    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();


    // 유저 댓글 목록 관리
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setUser(this);
    }

    // 유저 댓글 목록 관리
    public void removeComment(Comment comment){
        comments.remove(comment);
        comment.setUser(null);
    }

    // 유저 포스트 목록 관리
    public void addPost(Post post){
        posts.add(post);
        post.setUser(this);
    }

    // 유저 포스트 목록 관리
    public void removePost(Post post){
        posts.remove(post);
        post.setUser(null);
    }

}