package com.example.storywave.Service;

import com.example.storywave.Dto.BoardDto;
import com.example.storywave.Dto.CategoryDto;
import com.example.storywave.Dto.PostDto;
import com.example.storywave.Dto.PostListDto;
import com.example.storywave.Entity.Board;
import com.example.storywave.Entity.Category;
import com.example.storywave.Entity.Image;
import com.example.storywave.Entity.Post;
import com.example.storywave.Repository.BoardRepository;
import com.example.storywave.Repository.CategoryRepository;
import com.example.storywave.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<PostListDto> getPostSummaries(Long post_type_id) {
        List<Post> posts = postRepository.findAll(); // 모든 게시글을 가져옵니다.

        return posts.stream()
                .filter(post ->
                        post.getCategories().stream().anyMatch(category ->
                                category.getBoard().getPostTypeId().equals(0L) ||
                                        category.getBoard().getPostTypeId().equals(post_type_id)
                        )
                )
                .map(post -> {
                    Long commentCount = postRepository.countCommentsByPostId(post.getId()); // 댓글 수 계산
                    Set<CategoryDto> categoryDtos = post.getCategories().stream()
                            .map(category -> new CategoryDto(
                                    category.getId(),
                                    new BoardDto(category.getBoard().getPostTypeId(), category.getBoard().getViewPost()), // BoardDto 생성
                                    category.getName()))
                            .collect(Collectors.toSet());
                    return new PostListDto(
<<<<<<< HEAD
                            post.getId(),
                            post.getTitle(),
                            post.getUpdated_at(),
                            post.getThumbs(),
=======
                                                post.getId(),
                                                post.getTitle(),
                                                post.getUpdatedAt(),
                                                post.getThumbs(),
>>>>>>> upstream/demo_develope
                            categoryDtos, // 댓글 수를 설정합니다.
                            commentCount // 올바른 타입으로 설정
                    );
                })
                .collect(Collectors.toList());
    }

    public Post createPost(Post post, MultipartFile[] imageFiles, List<String> categoryNames, Long post_type_id, Integer thumbs) {
        // Board 설정
        Board board = boardRepository.findByPostTypeId(post_type_id).orElseGet(() -> {
            Board newBoard = new Board();
            newBoard.setPostTypeId(post_type_id);
            // 새로운 Board 생성 시 필요한 다른 필드들도 설정합니다.
            // 예: newBoard.setName("Default Board Name");
            // 필요에 따라 다른 필드들을 설정합니다.
            return boardRepository.save(newBoard);
        });
        post.setBoard(board);

        // Thumbs 설정
        post.setThumbs(thumbs != null ? thumbs : 0);

        // Categories 설정
        Set<Category> categories = new HashSet<>();
        for (String categoryName : categoryNames) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(categoryName);
                        newCategory.setBoard(board); // 카테고리에도 Board 설정
                        return categoryRepository.save(newCategory);
                    });
            categories.add(category);
        }
        post.setCategories(categories);

        if (post.getContent() != null) {
            String originalContent = post.getContent();
            System.out.println("Original content length: " + originalContent.length());

            String updatedContent = extractAndSaveImages(originalContent, post);
            post.setContent(updatedContent);

            System.out.println("Updated content length: " + updatedContent.length());
        }
        Post savedPost = postRepository.save(post);
        System.out.println("Saved post content length: " + savedPost.getContent().length());
        return postRepository.save(post);
    }

    private String extractAndSaveImages(String content, Post post) {
        String imagePattern = "<img[^>]+src\\s*=\\s*['\"]data:image/[^;]+;base64,([^'\"]+)['\"][^>]*>";
        Pattern pattern = Pattern.compile(imagePattern);
        Matcher matcher = pattern.matcher(content);
        StringBuilder updatedContent = new StringBuilder();

        int lastMatchEnd = 0;
        int imageCount = 0;
        while (matcher.find()) {
            imageCount++;
            String base64Image = matcher.group(1);

            String imagePath = saveImage(base64Image, post);

            // 로컬 파일 시스템 경로 대신 웹 서버에서 접근 가능한 URL 경로로 설정
            String fileName = new File(imagePath).getName(); // 이미지 파일명 추출
            updatedContent.append(content, lastMatchEnd, matcher.start());
            updatedContent.append("<img src=\"").append("/images/" + fileName).append("\" />");
            lastMatchEnd = matcher.end();
        }
        updatedContent.append(content.substring(lastMatchEnd));

        return updatedContent.toString();
    }

<<<<<<< HEAD
    private String saveImage(String base64Image, Post post) {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        System.out.println("Decoded image size: " + imageBytes.length + " bytes");

        String fileName = UUID.randomUUID().toString() + ".jpg";
        File file = new File(uploadDir + fileName);

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            FileUtils.writeByteArrayToFile(file, imageBytes);
            System.out.println("Image saved to file: " + file.getAbsolutePath());

            Image image = new Image();
            image.setUrl(file.getAbsolutePath());
            image.setPost(post);
            post.getImages().add(image);

            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save image: " + e.getMessage());
            throw new RuntimeException("Failed to save image", e);
        }
=======
    // 게시물 상세 조회
    public PostDto getPostByPostTypeIdAndPostId(Long post_type_id, Long postId) {
    return postRepository
            .findByBoard_PostTypeIdAndId(post_type_id, postId)
            .map(PostDto::fromPost)
            .orElseThrow(() -> new IllegalArgumentException("포스트를 찾을 수 없습니다."));
>>>>>>> upstream/demo_develope
    }
}