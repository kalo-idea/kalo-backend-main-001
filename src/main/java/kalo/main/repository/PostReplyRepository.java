package kalo.main.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import kalo.main.domain.PostReply;

public interface PostReplyRepository extends JpaRepository<PostReply, Long> {
    List<PostReply> findByPostIdAndDeleted(Long postId, Pageable pageable, Boolean deleted);
}
