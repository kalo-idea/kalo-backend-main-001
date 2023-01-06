package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Auths;

public interface AuthsRepository extends JpaRepository<Auths, Long> {    
}
