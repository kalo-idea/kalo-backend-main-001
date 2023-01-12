package kalo.main.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import kalo.main.domain.dto.petition.PetitionCondDto;
import kalo.main.domain.dto.petition.ReadSimplePetitionsDto;

public interface PetitionRepositoryCustom {
    List<ReadSimplePetitionsDto> findListPetitions(Pageable pageable, PetitionCondDto cond);
    List<ReadSimplePetitionsDto> findLikePetitions(Pageable pageable, Long viewerId);
    List<ReadSimplePetitionsDto> findSupportPetitions(Pageable pageable, Long viewerId);
}
