package com.example.storywave.Repository;

import java.util.List;
import java.util.Optional;

import com.example.storywave.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findByPost_Id(Long id);

  Optional<Comment> findByPost_IdAndCommentId(Long postId, Long commentId);
}
