package kalo.main.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import kalo.main.domain.dto.petition.ImportantPetitionResDto;
import kalo.main.domain.dto.petition.PetitionCondDto;
import kalo.main.domain.dto.petition.ReadSimplePetitionsDto;
import kalo.main.domain.dto.petition.SupportPetitionUserListDto;

public interface PetitionRepositoryCustom {
    List<ReadSimplePetitionsDto> findListPetitions(Pageable pageable, PetitionCondDto cond, Boolean recent);
    List<ReadSimplePetitionsDto> findLikePetitions(Pageable pageable, Long viewerId);
    List<ReadSimplePetitionsDto> findSupportPetitions(Pageable pageable, Long viewerId);
    List<SupportPetitionUserListDto> findSupportPetitionUserList(Pageable pageable, Long petitionId);
    List<ImportantPetitionResDto> getImportantPetitions(Pageable pageable);
}
