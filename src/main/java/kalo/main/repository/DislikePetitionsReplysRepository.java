package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.DislikePetitionsReplys;

public interface DislikePetitionsReplysRepository extends JpaRepository<DislikePetitionsReplys, Long> {
    
}
