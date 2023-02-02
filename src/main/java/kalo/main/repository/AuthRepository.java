package kalo.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Auth;

public interface AuthRepository extends JpaRepository<Auth, Long> {   
    Optional<Auth> findByKakaoAndDeleted(String kakao, Boolean deleted); 
}
