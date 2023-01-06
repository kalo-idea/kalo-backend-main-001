package kalo.main.repository;

import java.util.List;

import kalo.main.domain.Hashtags;

public interface HashtagsRepositoryCustom {
    List<Hashtags> findPostsHashtags(Long postId);
}
