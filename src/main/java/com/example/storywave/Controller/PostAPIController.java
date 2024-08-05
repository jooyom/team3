package com.example.storywave.Controller;

import com.example.storywave.Dto.PostDto;
import com.example.storywave.Dto.PostListDto;
import com.example.storywave.Entity.Post;
import com.example.storywave.Mapper.PostMapper;
import com.example.storywave.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("/post")
public class PostAPIController {
  private final PostService postService;

  @Autowired
  public PostAPIController(PostService postService) {
    this.postService = postService;
  }

    @PostMapping("/{post_type_id}")
    public ResponseEntity<PostDto> createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("categories") List<String> categoryNames,
            @RequestPart(value = "images", required = false) MultipartFile[] imageFiles,
            @RequestParam(value = "thumbs", required = false) Integer thumbs,
            @PathVariable("post_type_id") Long post_type_id) {

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);

        // dto에서 엔티티로 변환
        Post createdPost = postService.createPost(post, imageFiles, categoryNames, post_type_id, thumbs);


        // 엔티티에서 Dto로 변환
        PostDto postDto = PostMapper.INSTANCE.postToPostDto(createdPost);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/{post_type_id}")
    public ResponseEntity<List<PostListDto>> getPostSummaries(@PathVariable("post_type_id") Long post_type_id) {
        List<PostListDto> postSummaries = postService.getPostSummaries(post_type_id);
        return ResponseEntity.ok(postSummaries);
    }
}
