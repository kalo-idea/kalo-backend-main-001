package kalo.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.Timeline;

public interface TimelineRepository extends JpaRepository<Timeline, Long>, TimelineRepositoryCustom {
    List<Timeline> findByIdInAndDeletedOrderByAtTime(List<Long> id, Boolean deleted);
}
