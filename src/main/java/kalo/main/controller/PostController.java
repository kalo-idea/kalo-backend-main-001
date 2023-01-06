package kalo.main.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kalo.main.domain.dto.posts.CreatePostsDto;
import kalo.main.domain.dto.posts.ViewPostsDto;
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
    public void createPosts(@RequestBody CreatePostsDto createPostsDto) {
        log.info("at controller userId:{}", createPostsDto.getUsersId());
        postsService.createPosts(createPostsDto);
    }

    // 글 단건 조회
    @PostMapping("/posts/{postId}")
    public ViewPostsDto viewPosts(@PathVariable Long postId, @RequestBody OnlyUserIdDto onlyUserIdDto) {
        return postsService.viewPosts(postId, onlyUserIdDto.getUserId());
    }
}
