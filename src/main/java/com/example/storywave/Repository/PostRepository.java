package com.example.storywave.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.storywave.Entity.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
  @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
  Long countCommentsByPostId(@Param("postId") Long postId);

  Optional<Post> findByBoard_PostTypeIdAndId(Long postTypeId, Long id);
}
