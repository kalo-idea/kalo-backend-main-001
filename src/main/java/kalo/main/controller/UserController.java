package kalo.main.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kalo.main.domain.User;
import kalo.main.domain.dto.OnlyIdDto;
import kalo.main.domain.dto.petition.ReadPetitionsDto;
import kalo.main.domain.dto.user.JoinReqDto;
import kalo.main.domain.dto.user.MyProfileHomeDto;
import kalo.main.domain.dto.user.UpdateUserInfoReqDto;
import kalo.main.domain.dto.user.UpdateUserProfileReqDto;
import kalo.main.domain.dto.user.UserAuthResDto;
import kalo.main.domain.dto.user.UserInfoDto;
import kalo.main.domain.dto.user.UserProfileResDto;
import kalo.main.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 좋아요 한 청원 리스트
    @GetMapping("/get-like-petitions")
    public List<ReadPetitionsDto> readLikePetitions(
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, @Valid OnlyIdDto id) {
            return userService.getLikePetitions(pageable, id.getId());
    }

    // 참여한 청원 리스트
    @GetMapping("/get-support-petitions")
    public List<ReadPetitionsDto> readSupportPetitions(
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, @Valid OnlyIdDto id) {
            return userService.getSupportPetitions(pageable, id.getId());
    }

    // 카카오로 회원, 비회원 조회
    @GetMapping("/public/get-auth-by-kakao")
    public UserAuthResDto getAuthByKakao(@RequestParam(required = true) String kakao) {
        return userService.getAuthAndUserByKakao(kakao);
    }

    // 닉네임 검증 (현재 대소문자 판단만 구현)
    @GetMapping("/public/is-duplicated-nickname")
    public Boolean isDuplicatedNickname(@RequestParam(required = true) String nickname) {
        
        return userService.isDuplicatedNickname(nickname);
    }

    // 회원 가입
    @PostMapping("/public/create-auth")
    public UserAuthResDto createAuth(@RequestBody JoinReqDto req) {
        return userService.createAuth(req);
    }

    // 회원 탈퇴
    @PostMapping("/delete-auth")
    public Long deleteAuth(@RequestBody OnlyIdDto req) {
        return userService.deleteAuth(req.getId());
    }

    // 회원 프로필 정보
    @GetMapping("/public/get-user-profile")
    public UserProfileResDto getUserProfile(@RequestParam Long id) {
        return userService.getUserProfile(id);
    }

    // 본인 프로필 정보
    @GetMapping("/get-my-user-profile")
    public UserProfileResDto getMyUserProfile(@RequestParam Long id) {
        return userService.getMyProfile(id);
    }


    // 회원 정보 수정
    @PostMapping("/update-auth-info")
    public Long updateInfo(@RequestBody UpdateUserInfoReqDto req) {
        return userService.updateInfo(req);
    }

    // 유저 프로필 업데이트
    @PostMapping("/update-user-profile")
    public UserInfoDto updateUserProfile(@Valid @RequestBody UpdateUserProfileReqDto req) {
        return userService.updateUserPublicInfos(req);
    }

    // 유저 프로필 홈
    @GetMapping("/get-my-profile-home")
    public MyProfileHomeDto getProfileHome(@RequestParam Long id) {
        return userService.getProfileHome(id);
    }
}
