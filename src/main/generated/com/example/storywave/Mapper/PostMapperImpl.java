package com.example.storywave.Mapper;

import com.example.storywave.Dto.PostDto;
import com.example.storywave.Entity.Post;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-01T19:45:54+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
public class PostMapperImpl implements PostMapper {

    @Override
    public PostDto postToPostDto(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDto postDto = new PostDto();

        return postDto;
    }
}
