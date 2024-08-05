package com.example.storywave.Dto;

import com.example.storywave.Entity.Comment;
import com.example.storywave.Entity.Image;
import com.example.storywave.Entity.Post;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link Post}
 */
//@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PostDto implements Serializable {
    private Long postId;
    private Long postTypeId;
    private String title;
    private String content;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer thumbs;
    private List<ImageDto> images;
    private Set<CategoryDto> categories;
    private List<CommentDto> comments = new ArrayList<>();

    public static PostDto fromPost(Post post){
        PostDto postDto = PostDto.builder()
                .postId(post.getId())
                .postTypeId(post.getBoard().getPostTypeId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUser().getUserId())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .thumbs(post.getThumbs())
                .build();

        if (post.getComments() != null) {
            postDto.setComments(
                    post.getComments()
                            .stream()
                            .map(CommentDto::fromComment)
                            .collect(Collectors.toList()));
        }

        if (post.getImages() != null) {
            postDto.setImages(
                    post.getImages()
                            .stream().map(ImageDto::fromImage)
                            .collect(Collectors.toList()));
        }

        return postDto;
    }

    public Post toPost(){
        Post post = new Post();
        post.setId(this.postId);
        post.setTitle(this.title);
        post.setContent(this.content);
        post.setCreatedAt(this.createdAt);
        post.setUpdatedAt(this.updatedAt);
        post.setThumbs(this.thumbs);

        if (this.comments != null) {
            this.comments.forEach(
                    commentDto -> {
                        Comment comment = commentDto.toComment();
                        post.getComments().add(comment);
                    });
        }

        if (this.images != null) {
            this.images.forEach(
                    imageDto -> {
                        Image image = imageDto.toImage();
                        post.getImages().add(image);
                    }
            );
        }

        return post;
    }
}