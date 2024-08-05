package com.example.storywave.Dto;

import com.example.storywave.Entity.Image;
import com.example.storywave.Entity.Post;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Image}
 */
//@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ImageDto implements Serializable {
    private Long imageId;
    private String url;
//    private Post post;
    private Long postId;
    private LocalDateTime uploadedAt;

    public static ImageDto fromImage(Image image) {
        return ImageDto.builder()
                .imageId(image.getId())
                .url(image.getUrl())
                .postId(image.getPost().getId())
                .uploadedAt(image.getUploaded_at())
                .build();
    }

    public Image toImage(){
        Image image = new Image();
        image.setId(imageId);
        image.setUrl(url);
        image.setUploaded_at(uploadedAt);
        return image;
    }
}