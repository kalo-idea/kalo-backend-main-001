package kalo.main.repository;

import java.util.List;

import kalo.main.domain.dto.user.UserAuthResDto;

public interface UserRepositoryCustom {
    
    List<UserAuthResDto> getAuthKakao(String kakao);
}
