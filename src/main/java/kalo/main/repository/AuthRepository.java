package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Auth;

public interface AuthRepository extends JpaRepository<Auth, Long> {    
}
