package com.ormi.storywave.users;

import com.ormi.storywave.posts.PostDto;
import com.ormi.storywave.posts.PostRepository;
import com.ormi.storywave.posts.Posts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // userId가 있어야지 포스트 생성 가능
    public PostDto createPosts(PostDto postDto, String userId) {
        Posts posts = postDto.toPost();
        Users users = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user not found"));
        posts.setThumbs(0);
        posts.setCreatedAt(LocalDateTime.now());
        Posts savedPosts = postRepository.save(posts);
        users.addPost(savedPosts);
        return PostDto.fromPost(savedPosts);
    }

    @Transactional(readOnly = true)
    public Optional<PostDto> getPostById(Long id) {
        return postRepository.findById(id)
                .map(PostDto::fromPost);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostDto::fromPost)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostsByTitleContaining(String keyword) {
        return postRepository.findByTitleContaining(keyword).stream()
                .map(PostDto::fromPost)
                .collect(Collectors.toList());
    }

    // 글쓴이만 포스트 수정 가능
    public Optional<PostDto> updatePost(Long postId, PostDto updatePostDto, String userId) {
        return postRepository
                .findById(postId)
                .filter(posts -> posts.getUsers().getUserId().equals(userId))
                .map(
                        post -> {
                            post.setTitle(updatePostDto.getTitle());
                            post.setContent(updatePostDto.getContent());
                            post.setUpdatedAt(LocalDateTime.now());
                            return PostDto.fromPost(postRepository.save(post));
                        });
    }

    // 글쓴이나, role이 admin인 사람만 포스트 삭제 가능
    public boolean deletePosts(Long postId, String userId) {
        UsersDto users =
                userRepository.findById(userId)
                        .map(UsersDto::fromUsers)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));
        return postRepository
                .findById(postId)
                .filter(posts -> posts.getUsers().getUserId().equals(userId) || users.getRole().equals("admin"))
                .map(
                        posts -> {
                            postRepository.delete(posts);
                            return true;
                        })
                .orElse(false);
    }

    // 페이지 번호, 크기를 기반으로 페이지네이션된 게시물 반환 메서드
    public Page<Posts> findPaginated(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return postRepository.findAll(pageable);
    }
}
