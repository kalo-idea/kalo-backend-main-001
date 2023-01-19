package kalo.main.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import kalo.main.domain.dto.post.PostCondDto;
import kalo.main.domain.dto.post.ReadSimplePostDto;

public interface PostRespositoryCustom {
    List<ReadSimplePostDto> findListPosts(Pageable pageable, PostCondDto cond, Boolean recent);
}
