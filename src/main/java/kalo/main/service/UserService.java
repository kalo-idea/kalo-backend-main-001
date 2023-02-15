package kalo.main.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.controller.BasicException;
import kalo.main.domain.Auth;
import kalo.main.domain.Hashtag;
import kalo.main.domain.Media;
import kalo.main.domain.User;
import kalo.main.domain.dto.SimpleDeletedWriterDto;
import kalo.main.domain.dto.SimpleWriterDto;
import kalo.main.domain.dto.petition.ReadPetitionsDto;
import kalo.main.domain.dto.petition.ReadSimplePetitionsDto;
import kalo.main.domain.dto.user.JoinReqDto;
import kalo.main.domain.dto.user.MyProfileHomeDto;
import kalo.main.domain.dto.user.NicknameValidResDto;
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

            String progress = simplePetition.getProgress();
            if (progress.equals("unchecked")) {
                if (LocalDateTime.now().isAfter(simplePetition.getSupportingDateEnd())) {
                    if (simplePetition.getSupportCount() >= simplePetition.getGoal()) {
                        progress = "ongoing";
                    } else {
                        progress = "fail";
                    }
                } else {
                    progress = "recruit";
                }
            }
            simplePetition.setProgress(progress);

            List<String> steps = Arrays.asList(simplePetition.getStep().split(","));

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
            User user = userRepository.findById(simplePetition.getWriterId()).orElseThrow(() -> new BasicException("작성자를 찾을 수 없습니다."));
            SimpleWriterDto writer = !user.getDeleted() ? new SimpleWriterDto(user) : new SimpleDeletedWriterDto();
            result.add(new ReadPetitionsDto(simplePetition, writer, steps, words, fileNames));
        }

        return result;
    }

    // 유저 청원 참여 리스트조회
    public List<ReadPetitionsDto> getSupportPetitions(Pageable pageable, Long userId) {
        List<ReadSimplePetitionsDto> simplePetitions = petitionRepository.findSupportPetitions(pageable, userId);

        List<ReadPetitionsDto> result = new ArrayList<ReadPetitionsDto>();
        for (ReadSimplePetitionsDto simplePetition : simplePetitions) {
            String progress = simplePetition.getProgress();
            if (progress.equals("unchecked")) {
                if (LocalDateTime.now().isAfter(simplePetition.getSupportingDateEnd())) {
                    if (simplePetition.getSupportCount() >= simplePetition.getGoal()) {
                        progress = "ongoing";
                    } else {
                        progress = "fail";
                    }
                } else {
                    progress = "recruit";
                }
            }
            simplePetition.setProgress(progress);

            List<String> steps = Arrays.asList(simplePetition.getStep().split(","));

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

            User user = userRepository.findById(simplePetition.getWriterId()).orElseThrow(() -> new BasicException("작성자를 찾을 수 없습니다."));
            SimpleWriterDto writer = !user.getDeleted() ? new SimpleWriterDto(user) : new SimpleDeletedWriterDto();
            result.add(new ReadPetitionsDto(simplePetition, writer, steps, words, fileNames));
        }

        return result;
    }

    public UserAuthResDto getAuthAndUserByKakao(String kakao) {
        if (authRepository.findByKakaoAndDeleted(kakao, false).isPresent()) {
            // 이미 가입된 회원
            Auth auth = authRepository.findByKakaoAndDeleted(kakao, false).orElseThrow(() -> new BasicException("카카오 계정에 문제가 있습니다."));
            List<User> users = userRepository.findByAuthIdAndDeleted(auth.getId(), false).orElseThrow(() -> new BasicException("연결된 계정이 없습니다."));
            List<UserInfoDto> userInfos = new ArrayList<UserInfoDto>();
            for (User user : users) {
                userInfos.add(new UserInfoDto(user));
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
                .userInfos(userInfos)
                .build();

            return userAuthResDto;
        } else {
            // 회원가입 필요
            return UserAuthResDto.builder().build();
        }
    }

    // 유저 정보 공개 여부 선택
    public UserInfoDto updateUserPublicInfos(UpdateUserProfileReqDto req) {
        User user = userRepository.findById(req.getId()).get();
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

        return new UserInfoDto(user);
    }

    // 중복되면 true
    public Boolean isDuplicatedNickname(String nickname) {
        return userRepository.findByNicknameIgnoreCase(nickname).isPresent();
    }

    // 닉네임 유효성 체크
    public NicknameValidResDto isValidNickname(String nickname) {

        // 닉네임 입력
        if (nickname.length() == 0) {
            return new NicknameValidResDto("error", "닉네임을 입력해주세요.");
        }
        // 닉네임 길이
        if ((nickname.length() < 2)) {
            return new NicknameValidResDto("error", "닉네임은 최소 2자입니다.");
        }

        // 닉네임 길이
        if (nickname.length() > 10) {
            return new NicknameValidResDto("error", "닉네임은 최대 10자입니다.");
        }

        // 닉네임 불가능 문자
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎ가-힣!?@#$%^&*]{2,10}$");
        if (!pattern.matcher(nickname).find()) {
            return new NicknameValidResDto("error", "허용되지 않은 문자가 있습니다.");
        }

        // 닉네임 중복
        if (isDuplicatedNickname(nickname)) {
            return new NicknameValidResDto("error", "사용 중인 닉네임입니다.");
        }

        return new NicknameValidResDto("success", "사용 가능한 닉네임입니다.");
    }

    // 회원가입
    public UserAuthResDto createAuth(JoinReqDto req) {
        if (isDuplicatedNickname(req.getNickname())) {
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
        .build();
        authRepository.save(auth);

        User user = User.builder()
        .type("normal")
        .nickname(req.getNickname())
        .intro("")
        .profileSrc("")
        .publicInfos("region1depthName,region2depthName")
        .recentLogin(LocalDateTime.now())
        .auth(auth)
        .build();

        userRepository.save(user);

        List<UserInfoDto> userInfos = new ArrayList<UserInfoDto>();
        userInfos.add(new UserInfoDto(user));
        
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
            .userInfos(userInfos)
            .build();

        return userAuthResDto;
    }

    // 탈퇴
    public void deleteAuth(Long authId) {
        Auth auth = authRepository.findById(authId).orElseThrow(() -> new BasicException("계정을 찾을 수 없습니다."));
        List<User> users = userRepository.findByAuthIdAndDeleted(authId, false).orElseThrow(() -> new BasicException("연결된 계정이 없습니다."));
        for (User user : users) {
            user.delete();
        }
        auth.delete();
    }

    // 회원 정보 수정
    public Long updateInfo(UpdateUserInfoReqDto req) {
        Auth auth = authRepository.findById(req.getId()).orElseThrow(() -> new BasicException("없는 계정입니다."));

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
        res.setProfileSrc(user.getProfileSrc());

        List<String> publicInfos = Arrays.asList(user.getPublicInfos().split(","));
        res.setPublicInfos(publicInfos);

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
        List<String> publicInfos = Arrays.asList(user.getPublicInfos().split(","));

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
        res.setPublicInfos(publicInfos);
        res.setProfileSrc(user.getProfileSrc());
        

        return res;
    }

    // 프로필 홈 화면
    // 남은 포인트, 관심 청원 수, 참여 청원 수 반환
    public MyProfileHomeDto getProfileHome(Long userId) {
        Long ledgers = ledgerRepository.getSumUserLedger(userId);
        Long supportPetitionCount = supportPetitionRepository.countByUserId(userId);
        Long likePetitionCount = likePetitionRepository.countByUserIdAndDeleted(userId, false);
        
        if(ledgers == null) {
            ledgers = 0L;
        }

        return new MyProfileHomeDto(ledgers, supportPetitionCount, likePetitionCount);
    }
}
