package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.LikePetitionReply;

public interface LikePetitionReplyRepository extends JpaRepository<LikePetitionReply, Long> {
    Optional<LikePetitionReply> findByPetitionReplyIdAndUserIdAndDeleted(Long petitionReplyId, Long userId, Boolean deleted);
}
