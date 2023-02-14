package kalo.main.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import kalo.main.domain.Hashtag;
import kalo.main.domain.dto.petition.ReadSimplePetitionsDto;
import kalo.main.domain.dto.post.ReadSimplePostDto;

public interface HashtagRepositoryCustom {
    List<Hashtag> findPostHashtags(Long postId);
    List<Hashtag> findPetitionHashtags(Long petitionId);
    List<ReadSimplePetitionsDto> getPetitionsByHashtag(Pageable pageable, String hashtag);
    List<ReadSimplePostDto> getPostByHashtag(Pageable pageable, String hashtag);
}
