package kalo.main.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kalo.main.domain.dto.post.CreatePostDto;
import kalo.main.domain.dto.post.ViewPostDto;
import kalo.main.domain.dto.users.OnlyUserIdDto;
import kalo.main.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostsService postsService;

    // 글 작성
    @PostMapping("/posts")
    public void createPosts(@RequestBody CreatePostDto createPostsDto) {
        log.info("at controller userId:{}", createPostsDto.getUserId());
        postsService.createPosts(createPostsDto);
    }

    // 글 단건 조회
    @PostMapping("/posts/{postId}")
    public ViewPostDto viewPosts(@PathVariable Long postId, @RequestBody OnlyUserIdDto onlyUserIdDto) {
        return postsService.viewPosts(postId, onlyUserIdDto.getUserId());
    }
}
