package kalo.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.MediaTimeLine;
import kalo.main.domain.Timeline;

public interface MediaTimelineRepository extends JpaRepository<MediaTimeLine, Long> {
    List<MediaTimeLine> findByTimeline(Timeline timeline);    
}
