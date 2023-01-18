package kalo.main.controller;

import java.util.List;

import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kalo.main.domain.dto.ReplyDto;
import kalo.main.domain.dto.TargetIdUserIdDto;
import kalo.main.domain.dto.post.CreatePostDto;
import kalo.main.domain.dto.post.CreatePostReplyDto;
import kalo.main.domain.dto.post.PostCondDto;
import kalo.main.domain.dto.post.ReadPostDto;
import kalo.main.domain.dto.post.ReadPostsDto;
import kalo.main.service.PostService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 글 작성
    @PostMapping("/create-post")
    public void createPost(@RequestBody CreatePostDto createPostsDto) {
        postService.createPost(createPostsDto);
    }

    // 글 단건 조회
    @GetMapping("/public/get-post")
    public ReadPostDto readPost(TargetIdUserIdDto req) {
        return postService.readPost(req.getTargetId(), req.getUserId());
    }

    // 게시글 댓글 추가
    @PostMapping("/create-post-reply")
    public void createPostReply(@RequestBody CreatePostReplyDto createPostsDto) {
        postService.createPostReply(createPostsDto);
    }

    // 게시글 댓글 조회
    @GetMapping("/public/get-post-replys")
    public List<ReplyDto> readComments(
        @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
        TargetIdUserIdDto req) {
        return postService.readPostReply(req.getTargetId(), req.getUserId(), pageable);
    }

    @GetMapping("/public/get-posts")
    public List<ReadPostsDto> readPosts(
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
         PostCondDto cond
    ) {
        return postService.readPosts(pageable, cond);
    }
}
