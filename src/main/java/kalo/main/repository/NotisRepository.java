package kalo.main.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Notis;

public interface NotisRepository extends JpaRepository<Notis, Long> {
    List<Notis> findNotisByReceiverIdAndDeletedAndIsDisplay(Pageable pageable, Long receiverId, Boolean deleted, Boolean isDisplay);
    Long countNotisByReceiverIdAndDeletedAndIsDisplayAndIsCheck(Long receiverId, Boolean deleted, Boolean isDisplay, Boolean isCheck);
}