package kalo.main.repository;

import java.util.List;

import kalo.main.domain.Hashtag;

public interface HashtagRepositoryCustom {
    List<Hashtag> findPostHashtags(Long postId);
}
