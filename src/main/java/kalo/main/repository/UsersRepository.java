package kalo.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.User;

public interface UsersRepository extends JpaRepository<User, Long> {
    
}
