package kalo.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Petition;
import kalo.main.domain.TimelinePetition;

public interface TimelinePetitionRepository extends JpaRepository<TimelinePetition, Long> {
    List<TimelinePetition> findByPetition(Petition petition);
}
