package com.example.storywave.Repository;

import com.example.storywave.Entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUserId(String userId);

  Optional<User> findByPosts_Id(Long id);

  Optional<User> findByComments_CommentId(Long commentId);
}
