package kalo.main.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.admin.dto.AdminAuthDataDto;
import kalo.main.admin.dto.AdminAuthReqDto;
import kalo.main.admin.dto.AdminAuthResDto;
import kalo.main.admin.dto.AdminAuthsReqDto;
import kalo.main.admin.dto.AdminLedgerHistoryDto;
import kalo.main.admin.dto.AdminUserAuthReqDto;
import kalo.main.admin.dto.AdminUserAuthResDto;
import kalo.main.admin.dto.AdminUserReqDto;
import kalo.main.admin.dto.AdminUsersAuthsReqDto;
import kalo.main.admin.dto.AdminUserDataDto;
import kalo.main.controller.BasicException;
import kalo.main.domain.Auth;
import kalo.main.domain.Ledger;
import kalo.main.domain.User;
import kalo.main.domain.dto.SimpleWriterDto;
import kalo.main.repository.AuthRepository;
import kalo.main.repository.LedgerRepository;
import kalo.main.repository.PetitionRepository;
import kalo.main.repository.PostRepository;
import kalo.main.repository.UserRepository;
import kalo.main.service.LedgerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final PetitionRepository petitionRepository;
    private final PostRepository postRepository;
    private final AuthRepository authRepository;
    private final LedgerRepository ledgerRepository;
    private final LedgerService ledgerService;
    
    // 관리자용 거래내역 조회
    public List<AdminLedgerHistoryDto> getLedgersHistory(Pageable pageable, Long userId) {        
        List<Ledger> ledger = ledgerRepository.findByUserIdOrderByCreatedDateDesc(userId).get();
        List<AdminLedgerHistoryDto> history = new ArrayList<AdminLedgerHistoryDto>();
        for (Ledger led : ledger) {
            history.add(new AdminLedgerHistoryDto(led));
        }

        return history;
    }
    
    // Auth 조회
    public AdminAuthResDto getAuth(AdminAuthReqDto req) {
        Auth auth = authRepository.findById(req.getAuthId()).orElseThrow(() -> new BasicException("없는 계정입니다."));
        AdminAuthResDto res = new AdminAuthResDto();
        res.setId(auth.getId());
        
        if (req.getType()) {
            res.setType(auth.getType());
        }
        if (req.getKakao()) {
            res.setKakao(auth.getKakao());
        }
        if (req.getEmail()) {
            res.setEmail(auth.getEmail());
        }
        if (req.getName()) { 
            res.setName(auth.getName());
        }
        if (req.getBirth()) {
            res.setBirth(auth.getBirth());
        }
        if (req.getGender()) {
            res.setGender(auth.getGender());
        }
        if (req.getTel()) {
            res.setTel(auth.getTel());
        }
        if (req.getAddress()) {
            res.setAddress(auth.getAddress());
        }
        if (req.getRegion1depthName()) {
            res.setRegion1depthName(auth.getRegion1depthName());
        }
        if (req.getRegion2depthName()) {
            res.setRegion2depthName(auth.getRegion2depthName());
        }
        if (req.getPromotionCheck()) {
            res.setPromotionCheck(auth.getPromotionCheck());
        }
        if (req.getFcmToken()) {
            res.setFcmToken(auth.getFcmToken());
        }
        res.setCreatedDate(auth.getCreatedDate());
        List<User> users = userRepository.findByAuthIdAndDeleted(req.getAuthId(), false).get();
        List<SimpleWriterDto> users_res = new ArrayList<SimpleWriterDto>();
        for (User user : users) {
            users_res.add(new SimpleWriterDto(user));
        }
        res.setUsers(users_res);

        return res;
    }

    // auths 조회
    public List<AdminAuthResDto> getAuths(Pageable pageable, AdminAuthsReqDto req) {
        Page<Auth> auths = authRepository.findAll(pageable);
        List<AdminAuthResDto> result = new ArrayList();
        for (Auth auth : auths) {
            AdminAuthResDto res = new AdminAuthResDto();
            res.setId(auth.getId());
        
            if (req.getType()) {
                res.setType(auth.getType());
            }
            if (req.getKakao()) {
                res.setKakao(auth.getKakao());
            }
            if (req.getEmail()) {
                res.setEmail(auth.getEmail());
            }
            if (req.getName()) { 
                res.setName(auth.getName());
            }
            if (req.getBirth()) {
                res.setBirth(auth.getBirth());
            }
            if (req.getGender()) {
                res.setGender(auth.getGender());
            }
            if (req.getTel()) {
                res.setTel(auth.getTel());
            }
            if (req.getAddress()) {
                res.setAddress(auth.getAddress());
            }
            if (req.getRegion1depthName()) {
                res.setRegion1depthName(auth.getRegion1depthName());
            }
            if (req.getRegion2depthName()) {
                res.setRegion2depthName(auth.getRegion2depthName());
            }
            if (req.getPromotionCheck()) {
                res.setPromotionCheck(auth.getPromotionCheck());
            }
            if (req.getFcmToken()) {
                res.setFcmToken(auth.getFcmToken());
            }
            res.setCreatedDate(auth.getCreatedDate());
            
            List<User> users = userRepository.findByAuthIdAndDeleted(auth.getId(), false).get();
            List<SimpleWriterDto> users_res = new ArrayList<SimpleWriterDto>();
            for (User user : users) {
                users_res.add(new SimpleWriterDto(user));
            }
            res.setUsers(users_res);

            result.add(res);
        }
        
        return result;
    }

    // Auth 수정
    public String updateAuth(AdminAuthDataDto req) {
        Auth res = authRepository.findById(req.getId()).orElseThrow(() -> new BasicException("없는 계정입니다."));

        if (req.getType() != null) {
            res.setType(req.getType());
        }
        if (req.getKakao() != null) {
            res.setKakao(req.getKakao());
        }
        if (req.getEmail() != null) {
            res.setEmail(req.getEmail());
        }
        if (req.getName() != null) {
            res.setName(req.getName());
        }
        if (req.getBirth() != null) {
            res.setBirth(req.getBirth());
        }
        if (req.getGender() != null) {
            res.setGender(req.getGender());
        }
        if (req.getTel() != null) {
            res.setTel(req.getTel());
        }
        if (req.getAddress() != null) {
            res.setAddress(req.getAddress());
        }
        if (req.getRegion1depthName() != null) {
            res.setRegion1depthName(req.getRegion1depthName());
        }
        if (req.getRegion2depthName() != null) {
            res.setRegion2depthName(req.getRegion2depthName());
        }
        if (req.getPromotionCheck() != null) {
            res.setPromotionCheck(req.getPromotionCheck());
        }
        if (req.getFcmToken() != null) {
            res.setFcmToken(req.getFcmToken());
        }

        return "success";
    }

    // User 조회
    public AdminUserDataDto getUser(AdminUserReqDto req) {
        User user = userRepository.findById(req.getUserId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        AdminUserDataDto res = new AdminUserDataDto();
        
        res.setId(user.getId());
        if (req.getType()) {
            res.setType(user.getType());
        }
        if (req.getNickname()) {
            res.setNickname(user.getNickname());
        }
        if (req.getIntro()) {
            res.setIntro(user.getIntro());
        }
        if (req.getProfileSrc()) {
            res.setProfileSrc(user.getProfileSrc());
        }
        if (req.getPublicInfos()) {
            res.setPublicInfos(user.getPublicInfos());
        }
        if (req.getLedger()) {
            res.setLedger(ledgerService.getPoint(user.getId()));
        }

        return res;
    }

    // User 수정
    public String updateUser(AdminUserDataDto req) {
        User res = userRepository.findById(req.getId()).orElseThrow(() -> new BasicException("없는 유저입니다."));

        if (req.getType() != null) {
            res.setType(req.getType());
        }
        if (req.getNickname() != null) {
            res.setNickname(req.getNickname());
        }
        if (req.getIntro() != null) {
            res.setIntro(req.getIntro());
        }
        if (req.getProfileSrc() != null) {
            res.setProfileSrc(req.getProfileSrc());
        }
        if (req.getPublicInfos() != null) {
            res.setPublicInfos(req.getPublicInfos());
        }

        return "성공";
    }

    // User, Auth 조회
    public AdminUserAuthResDto getUserAndAuth(AdminUserAuthReqDto req) {
        User user = userRepository.findById(req.getUserId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        Auth auth = authRepository.findById(user.getAuth().getId()).orElseThrow(() -> new BasicException("없는 계정입니다."));
        
        AdminUserAuthResDto res = new AdminUserAuthResDto();
        
        res.setUserId(user.getId());
        if (req.getUserType()) {
            res.setUserType(user.getType());
        }
        if (req.getNickname()) {
            res.setNickname(user.getNickname());
        }
        if (req.getIntro()) {
            res.setIntro(user.getIntro());
        }
        if (req.getProfileSrc()) {
            res.setProfileSrc(user.getProfileSrc());
        }
        if (req.getPublicInfos()) {
            res.setPublicInfos(user.getPublicInfos());
        }
        if (req.getLedger()) {
            res.setLedger(ledgerService.getPoint(user.getId()));
        }

        res.setAuthId(auth.getId());
        
        if (req.getAuthType()) {
            res.setAuthType(auth.getType());
        }
        if (req.getKakao()) {
            res.setKakao(auth.getKakao());
        }
        if (req.getEmail()) {
            res.setEmail(auth.getEmail());
        }
        if (req.getName()) { 
            res.setName(auth.getName());
        }
        if (req.getBirth()) {
            res.setBirth(auth.getBirth());
        }
        if (req.getGender()) {
            res.setGender(auth.getGender());
        }
        if (req.getTel()) {
            res.setTel(auth.getTel());
        }
        if (req.getAddress()) {
            res.setAddress(auth.getAddress());
        }
        if (req.getRegion1depthName()) {
            res.setRegion1depthName(auth.getRegion1depthName());
        }
        if (req.getRegion2depthName()) {
            res.setRegion2depthName(auth.getRegion2depthName());
        }
        if (req.getPromotionCheck()) {
            res.setPromotionCheck(auth.getPromotionCheck());
        }
        if (req.getFcmToken()) {
            res.setFcmToken(auth.getFcmToken());
        }
        res.setCreatedDate(auth.getCreatedDate());

        return res;
    }

    // Users, Auths 조회
    public List<AdminUserAuthResDto> getUsersAndAuths(Pageable pageable, AdminUsersAuthsReqDto req) {
        Page<User> users = userRepository.findAll(pageable);
        List<AdminUserAuthResDto> result = new ArrayList();
        System.out.println("users : " + users);
        for (User user : users) {
            Auth auth = authRepository.findById(user.getAuth().getId()).orElseThrow(() -> new BasicException("없는 계정입니다."));
            
            System.out.println("auth : " + auth);
            AdminUserAuthResDto res = new AdminUserAuthResDto();
            
            res.setUserId(user.getId());
            if (req.getUserType()) {
                res.setUserType(user.getType());
            }
            if (req.getNickname()) {
                res.setNickname(user.getNickname());
            }
            if (req.getIntro()) {
                res.setIntro(user.getIntro());
            }
            if (req.getProfileSrc()) {
                res.setProfileSrc(user.getProfileSrc());
            }
            if (req.getPublicInfos()) {
                res.setPublicInfos(user.getPublicInfos());
            }
            if (req.getLedger()) {
                res.setLedger(ledgerService.getPoint(user.getId()));
            }
            if (req.getRecentLogin()) {
                res.setRecentLogin(user.getRecentLogin());
            }

            res.setAuthId(auth.getId());
            
            if (req.getAuthType()) {
                res.setAuthType(auth.getType());
            }
            if (req.getKakao()) {
                res.setKakao(auth.getKakao());
            }
            if (req.getEmail()) {
                res.setEmail(auth.getEmail());
            }
            if (req.getName()) { 
                res.setName(auth.getName());
            }
            if (req.getBirth()) {
                res.setBirth(auth.getBirth());
            }
            if (req.getGender()) {
                res.setGender(auth.getGender());
            }
            if (req.getTel()) {
                res.setTel(auth.getTel());
            }
            if (req.getAddress()) {
                res.setAddress(auth.getAddress());
            }
            if (req.getRegion1depthName()) {
                res.setRegion1depthName(auth.getRegion1depthName());
            }
            if (req.getRegion2depthName()) {
                res.setRegion2depthName(auth.getRegion2depthName());
            }
            if (req.getPromotionCheck()) {
                res.setPromotionCheck(auth.getPromotionCheck());
            }
            if (req.getFcmToken()) {
                res.setFcmToken(auth.getFcmToken());
            }
            res.setCreatedDate(auth.getCreatedDate());

            result.add(res);
        }

        return result;
    }
}
