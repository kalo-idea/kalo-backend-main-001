package kalo.main.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.controller.BasicException;
import kalo.main.domain.Auth;
import kalo.main.domain.Hashtag;
import kalo.main.domain.Media;
import kalo.main.domain.User;
import kalo.main.domain.dto.SimpleWriterDto;
import kalo.main.domain.dto.petition.ReadPetitionsDto;
import kalo.main.domain.dto.petition.ReadSimplePetitionsDto;
import kalo.main.domain.dto.user.JoinReqDto;
import kalo.main.domain.dto.user.MyProfileHomeDto;
import kalo.main.domain.dto.user.UpdateUserInfoReqDto;
import kalo.main.domain.dto.user.UpdateUserProfileReqDto;
import kalo.main.domain.dto.user.UserAuthResDto;
import kalo.main.domain.dto.user.UserInfoDto;
import kalo.main.domain.dto.user.UserProfileResDto;
import kalo.main.repository.AuthRepository;
import kalo.main.repository.HashtagRepository;
import kalo.main.repository.LedgerRepository;
import kalo.main.repository.LikePetitionRepository;
import kalo.main.repository.MediaRepository;
import kalo.main.repository.PetitionRepository;
import kalo.main.repository.SupportPetitionRepository;
import kalo.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PetitionRepository petitionRepository;
    private final HashtagRepository hashtagRepository;
    private final AuthRepository authRepository;
    private final MediaRepository mediaRepository;
    private final LedgerRepository ledgerRepository;
    private final SupportPetitionRepository supportPetitionRepository;
    private final LikePetitionRepository likePetitionRepository;

    // 유저 청원 좋아요 리스트조회
    public List<ReadPetitionsDto> getLikePetitions(Pageable pageable, Long userId) {
        List<ReadSimplePetitionsDto> simplePetitions = petitionRepository.findLikePetitions(pageable, userId);

        List<ReadPetitionsDto> result = new ArrayList<ReadPetitionsDto>();
        for (ReadSimplePetitionsDto simplePetition : simplePetitions) {
            User writer = userRepository.findById(simplePetition.getWriterId()).get();
            List<String> words = new ArrayList<String>();
            List<Hashtag> hashtags = hashtagRepository.findPetitionHashtags(simplePetition.getPetitionId());
            for (Hashtag hashtag : hashtags) {
                words.add(hashtag.getWord());
            }
            List<String> fileNames = new ArrayList<String>();
            List<Media> media = mediaRepository.findPetitionMedia(simplePetition.getPetitionId());
            for (Media medium : media) {
                fileNames.add(medium.getFileName());
            }
            if (writer.getDeleted()) {
                result.add(new ReadPetitionsDto(simplePetition, null, words, fileNames));
            }
            else {
                result.add(new ReadPetitionsDto(simplePetition, new SimpleWriterDto(writer.getId(), writer.getNickname(), writer.getProfileSrc()), words, fileNames));
            }
        }

        return result;
    }

    // 유저 청원 참여 리스트조회
    public List<ReadPetitionsDto> getSupportPetitions(Pageable pageable, Long userId) {
        List<ReadSimplePetitionsDto> simplePetitions = petitionRepository.findSupportPetitions(pageable, userId);

        List<ReadPetitionsDto> result = new ArrayList<ReadPetitionsDto>();
        for (ReadSimplePetitionsDto simplePetition : simplePetitions) {
            User writer = userRepository.findById(simplePetition.getWriterId()).get();
            List<String> words = new ArrayList<String>();
            List<Hashtag> hashtags = hashtagRepository.findPetitionHashtags(simplePetition.getPetitionId());
            for (Hashtag hashtag : hashtags) {
                words.add(hashtag.getWord());
            }
            List<String> fileNames = new ArrayList<String>();
            List<Media> media = mediaRepository.findPetitionMedia(simplePetition.getPetitionId());
            for (Media medium : media) {
                fileNames.add(medium.getFileName());
            }
            if (writer.getDeleted()) {
                result.add(new ReadPetitionsDto(simplePetition, null, words, fileNames));
            }
            else {
                result.add(new ReadPetitionsDto(simplePetition, new SimpleWriterDto(writer.getId(), writer.getNickname(), writer.getProfileSrc()), words, fileNames));
            }
        }

        return result;
    }

    public UserAuthResDto getAuthAndUserByKakao(String kakao) {
        if (authRepository.findByKakao(kakao).isPresent()) {
            // 이미 가입된 회원
            Auth auth = authRepository.findByKakao(kakao).get();
            List<User> users = userRepository.findByAuthId(auth.getId()).orElseThrow(() -> new BasicException("연결된 계정이 없습니다."));
            List<UserInfoDto> users_res = new ArrayList<UserInfoDto>();
            for (User user : users) {
                users_res.add(new UserInfoDto(user));
            }

            UserAuthResDto userAuthResDto = UserAuthResDto.builder()
            .authId(auth.getId())
            .type(auth.getType())
            .kakao(auth.getKakao())
            .email(auth.getEmail())
            .name(auth.getName())
            .birth(auth.getBirth())
            .gender(auth.getGender())
            .tel(auth.getTel())
            .address(auth.getAddress())
            .region1depthName(auth.getRegion1depthName())
            .region2depthName(auth.getRegion2depthName())
            .promotionCheck(auth.getPromotionCheck())
            .fcmToken(auth.getFcmToken())
            .recentLogin(auth.getRecentLogin())
            .userInfoDto(users_res)
            .build();

            return userAuthResDto;
        } else {
            // 회원가입 필요
            return null;
        }
    }

    // 유저 정보 공개 여부 선택
    public void updateUserPublicInfos(UpdateUserProfileReqDto req) {
        User user = userRepository.findById(req.getUserId()).get();
        Map<String, Boolean> map = req.getCheck();
        List<String> list = new ArrayList<String>();
        for (String key : map.keySet()) {
            if (map.get(key)) {
                list.add(key);
            }
        }
        String publicInfos = "";
        
        for (String str : list) {
            publicInfos = publicInfos + str + ',';
        }
        if (publicInfos.length() != 0 && publicInfos.charAt(publicInfos.length() - 1) == ',') {
            publicInfos = publicInfos.substring(0, publicInfos.length() - 1);
        }
        user.setProfileSrc(req.getProfile());
        user.setIntro(req.getIntro());
        user.setPublicInfos(publicInfos);
    }

    // 가능한 닉네임이면 true
    // 불가능한 닉네임이면 false
    public Boolean isDuplicatedNickname(String nickname) {
        return !userRepository.findByNicknameIgnoreCase(nickname).isPresent();
    }

    public Long join(JoinReqDto req) {
        if (!isDuplicatedNickname(req.getNickname())) {
            throw new BasicException("불가능한 닉네임입니다.");
        }
        Auth auth = Auth.builder()
        .type("normal")
        .kakao(req.getKakao())
        .email(req.getEmail())
        .name(req.getName())
        .birth(req.getBirth())
        .gender(req.getGender())
        .tel(req.getTel())
        .address("")
        .region1depthName(req.getRegion1depthName())
        .region2depthName(req.getRegion2depthName())
        .promotionCheck(req.getPromotionCheck())
        .fcmToken(req.getFcmToken())
        .recentLogin(LocalDateTime.now())
        .build();
        authRepository.save(auth);

        User user = User.builder()
        .type("normal")
        .nickname(req.getNickname())
        .intro("")
        .profileSrc("")
        .publicInfos("region1depthName,region2depthName")
        .auth(auth)
        .build();
        Long userId = userRepository.save(user).getId();

        return userId;
    }

    // 탈퇴
    public Long out(Long userId) {
        User user = userRepository.findById(userId).get();
        user.delete();
        return userId;
    }

    // 회원 정보 수정
    public Long updateInfo(UpdateUserInfoReqDto req) {
        Auth auth = authRepository.findById(req.getAuthId()).orElseThrow(() -> new BasicException("없는 계정입니다."));

        auth.setName(req.getName());
        auth.setBirth(req.getBirth());
        auth.setGender(req.getGender());
        auth.setEmail(req.getEmail());
        auth.setTel(req.getTel());
        auth.setRegion1depthName(req.getRegion1depthName());
        auth.setRegion2depthName(req.getRegion2depthName());
        auth.setPromotionCheck(req.getPromotionCheck());

        return auth.getId();
    }

    // 회원 정보 조회
    public UserProfileResDto getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("없는 회원입니다."));
        Auth auth = user.getAuth();             
        List<String> info = Arrays.asList(user.getPublicInfos().split(","));

        UserProfileResDto res = new UserProfileResDto();

        res.setUserId(userId);
        res.setNickname(user.getNickname());
        res.setPromotionCheck(auth.getPromotionCheck());
        res.setIntro(user.getIntro());

        if (info.contains("birth")) {
            res.setBirth(auth.getBirth());
        }
        if (info.contains("email")) {
            res.setEmail(auth.getEmail());
        }
        if (info.contains("gender")) {
            res.setGender(auth.getGender());
        }
        if (info.contains("name")) {
            res.setName(auth.getName());
        }
        if (info.contains("region1depthName")) {
            res.setRegion1depthName(auth.getRegion1depthName());
        }
        if (info.contains("region2depthName")) {
            res.setRegion2depthName(auth.getRegion2depthName());
        }
        if (info.contains("tel")) {
            res.setTel(auth.getTel());
        }

        return res;
    }

    // 본인 정보 조회
    public UserProfileResDto getMyProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("없는 회원입니다."));
        Auth auth = user.getAuth();
        
        UserProfileResDto res = new UserProfileResDto();

        res.setUserId(userId);
        res.setNickname(user.getNickname());
        res.setIntro(user.getIntro());
        res.setPromotionCheck(auth.getPromotionCheck());
        res.setBirth(auth.getBirth());
        res.setEmail(auth.getEmail());
        res.setGender(auth.getGender());
        res.setName(auth.getName());
        res.setRegion1depthName(auth.getRegion1depthName());
        res.setRegion2depthName(auth.getRegion2depthName());
        res.setTel(auth.getTel());

        return res;
    }

    // 프로필 홈 화면
    // 남은 포인트, 관심 청원 수, 참여 청원 수 반환
    public MyProfileHomeDto getProfileHome(Long userId) {
        Long ledgers = ledgerRepository.getSumUserLedger(userId);
        Long supportCount = supportPetitionRepository.countByUserId(userId);
        Long likeCount = likePetitionRepository.countByUserIdAndDeleted(userId, false);
        
        if(ledgers == null) {
            ledgers = 0L;
        }

        return new MyProfileHomeDto(ledgers, supportCount, likeCount);
    }
}
