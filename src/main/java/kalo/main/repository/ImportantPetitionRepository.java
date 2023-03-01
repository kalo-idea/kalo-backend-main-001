package kalo.main.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.ImportantPetition;

public interface ImportantPetitionRepository extends JpaRepository<ImportantPetition, Long>{
    List<ImportantPetition> findByImportantEndDateAfter(LocalDateTime nowtime);
}
