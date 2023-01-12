package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.DislikePetitionReply;

public interface DislikePetitionReplyRepository extends JpaRepository<DislikePetitionReply, Long> {
    Optional<DislikePetitionReply> findByPetitionReplyIdAndUserIdAndDeleted(Long petitionReplyId, Long userId, Boolean deleted);
}
