package com.example.storywave.Controller;

import com.example.storywave.Dto.*;
import com.example.storywave.Repository.CommentRepository;
import com.example.storywave.Repository.UserRepository;
import com.example.storywave.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/board")
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public PostController(PostService postService, UserRepository userRepository, CommentRepository commentRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    // 게시물 상세화면 조회
    @GetMapping("/{post_type_id}/post/{postId}")
    public String postsDetail( @PathVariable("post_type_id") Long postTypeId,
                               @PathVariable("postId") Long postId, @RequestParam("userId") String userId, Model model) {
        System.out.println("postsDetail 실행");
        // 게시물
        PostDto posts =
                postService
                        .getPostByPostTypeIdAndPostId(postTypeId, postId);

        UserDto users =
                userRepository
                        .findByUserId(userId)
                        .map(UserDto::fromUser)
                        .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        String writer = userRepository.findByPosts_Id(postId)
                .map(UserDto::fromUser)
                .map(UserDto::getNickname)
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트의 작성자를 찾을 수 없습니다."));

        // 권한 테스트를 위해 임시로 작성
        String role = users.getRole();

        model.addAttribute("writer", writer); // 글쓴이

        model.addAttribute("posts", posts); //게시물

        model.addAttribute("users", users); // 유저

        model.addAttribute("role", role); // 권한

        // 댓글을 ArrayList로 구현하여 ArrayList 메서드를 사용하였습니다.
        model.addAttribute("comments", posts.getComments().size()); // 댓글 개수

        return "board/posts_detail";
    }

}