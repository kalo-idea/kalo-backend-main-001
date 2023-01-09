package kalo.main.repository;

import kalo.main.domain.dto.post.GetPostWriter;

public interface PostRepositoryCustom {
    GetPostWriter findWriter(Long postId);
}
