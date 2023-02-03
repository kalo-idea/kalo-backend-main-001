package kalo.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNicknameIgnoreCase(String nickname);
    Optional<List<User>> findByAuthIdAndDeleted(Long authId, Boolean deleted);
    Optional<User> findByIdAndDeleted(Long id, Boolean deleted);
}
