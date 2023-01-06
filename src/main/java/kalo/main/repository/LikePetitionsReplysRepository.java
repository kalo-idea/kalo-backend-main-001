package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.LikePetitionsReplys;

public interface LikePetitionsReplysRepository extends JpaRepository<LikePetitionsReplys, Long> {
    
}
