package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    
}
