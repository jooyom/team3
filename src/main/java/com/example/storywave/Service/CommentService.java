package com.example.storywave.Service;

import com.example.storywave.Dto.UserDto;
import com.example.storywave.Entity.Post;
import com.example.storywave.Entity.User;
import com.example.storywave.Repository.PostRepository;
import com.example.storywave.Repository.UserRepository;
import com.example.storywave.Entity.Comment;
import com.example.storywave.Dto.CommentDto;
import com.example.storywave.Repository.CommentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  @Autowired
  public CommentService(
      CommentRepository commentRepository,
      PostRepository postRepository,
      UserRepository userRepository) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  public CommentDto createComment(CommentDto commentDto, Long postId, String userId) {
    Post posts =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));

    User users =
        userRepository
            .findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));

    Comment comment = commentDto.toComment();
    comment.setCreatedAt(LocalDateTime.now());
    comment.setPost(posts);

    Comment savedComment = commentRepository.save(comment);
    posts.addComment(savedComment);
    users.addComment(savedComment);

    return CommentDto.fromComment(savedComment);
  }

  // 모든 댓글 조회
  @Transactional(readOnly = true)
  public List<CommentDto> getAllComments(Long postId) {
    return commentRepository.findByPost_Id(postId).stream()
        .map(CommentDto::fromComment)
        .collect(Collectors.toList());
  }

  public CommentDto getCommentById(Long commentId) {
    return commentRepository
        .findById(commentId)
        .map(CommentDto::fromComment)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
  }

  // 댓글 작성자만 수정 가능
  public Optional<CommentDto> updateComment(
      Long postId, Long commentId, CommentDto commentDto, String userId) {
    return commentRepository
        .findByPost_IdAndCommentId(postId, commentId)
        .filter(comment -> comment.getUser().getUserId().equals(userId))
        .map(
            comment -> {
              comment.setContent(commentDto.getContent());
              comment.setUpdatedAt(LocalDateTime.now());
              return CommentDto.fromComment(commentRepository.save(comment));
            });
  }

  // 댓글 작성자나, 운영자만 댓글 삭제 가능
  public boolean deleteComment(Long postId, Long commentId, String userId) {
    UserDto users =
        userRepository
            .findByUserId(userId)
            .map(UserDto::fromUser)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
    return commentRepository
        .findByPost_IdAndCommentId(postId, commentId)
        .filter(
            posts -> posts.getUser().getUserId().equals(userId) || users.getRole().equals("admin"))
        .map(
            comment -> {
              commentRepository.delete(comment);
              return true;
            })
        .orElse(false);
  }
}
