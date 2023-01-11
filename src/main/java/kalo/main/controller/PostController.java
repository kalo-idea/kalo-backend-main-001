package kalo.main.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kalo.main.domain.dto.post.CreatePostDto;
import kalo.main.domain.dto.post.ReadPostDto;
import kalo.main.domain.dto.users.OnlyUserIdDto;
import kalo.main.service.PostService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postsService;

    // 글 작성
    @PostMapping("/post")
    public void createPosts(@RequestBody CreatePostDto createPostsDto) {
        postsService.createPost(createPostsDto);
    }

    // 글 단건 조회
    @PostMapping("/post/{postId}")
    public ReadPostDto readPosts(@PathVariable Long postId, @RequestBody OnlyUserIdDto onlyUserIdDto) {
        return postsService.viewPosts(postId, onlyUserIdDto.getUserId());
    }
}
