package kalo.main.repository;

import kalo.main.domain.dto.posts.GetPostsWriter;

public interface PostRepositoryCustom {
    GetPostsWriter findWriter(Long postId);
}
