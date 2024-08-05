package com.example.storywave.Dto;

import com.example.storywave.Entity.Comment;
import com.example.storywave.Entity.Post;
import com.example.storywave.Entity.User;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** DTO for {@link User} */
//@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto implements Serializable {
  String userId;
  String username;
  String password;
  String nickname;
  String email;
  String role;
  String activeStatus;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
  List<CommentDto> comments = new ArrayList<>();
  List<PostDto> posts = new ArrayList<>();

  public static UserDto fromUser(User user) {
    UserDto userDto =
        UserDto.builder()
            .userId(user.getUserId())
            .username(user.getUsername())
            .password(user.getPassword())
            .nickname(user.getNickname())
            .email(user.getEmail())
            .role(user.getRole())
            .activeStatus(user.getActiveStatus())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();

    if (user.getComments() != null) {
      userDto.setComments(
          user.getComments().stream().map(CommentDto::fromComment).collect(Collectors.toList()));
    }

    if (user.getPosts() != null) {
      userDto.setPosts(
          user.getPosts().stream().map(PostDto::fromPost).collect(Collectors.toList()));
    }
    return userDto;
  }

  public User toUser() {
    User user = new User();
    user.setUserId(this.userId);
    user.setUsername(this.username);
    user.setPassword(this.password);
    user.setNickname(this.nickname);
    user.setEmail(this.email);
    user.setRole(this.role);
    user.setActiveStatus(this.activeStatus);
    user.setCreatedAt(this.createdAt);
    user.setUpdatedAt(this.updatedAt);

    if (this.comments != null) {
      this.comments.forEach(
          commentDto -> {
            Comment comment = commentDto.toComment();
            user.getComments().add(comment);
          });
    }

    if (this.posts != null) {
      this.posts.forEach(
          postDto -> {
            Post post = postDto.toPost();
            user.getPosts().add(post);
          });
    }

    return user;
  }
}
