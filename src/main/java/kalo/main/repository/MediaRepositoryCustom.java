package kalo.main.repository;

import java.util.List;

import kalo.main.domain.Media;

public interface MediaRepositoryCustom {
    List<Media> findPostMedia(Long postId);
    List<Media> findPetitionMedia(Long petitionId);
}
