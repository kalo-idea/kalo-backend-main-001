package kalo.main.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.PetitionReply;

public interface PetitionReplyRepository extends JpaRepository<PetitionReply, Long> {
    List<PetitionReply> findByPetitionIdAndDeleted(Long petitionId, Pageable pageable, Boolean deleted);
}
