package kalo.main.admin;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    

    // 포인트 내역
    @GetMapping("/get-ledger")
    public List<AdminLedgerHistoryDto> getLedger(
        @PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
        @Valid Long userId) {
            return adminService.getLedgersHistory(pageable, userId);
    }

    // 계정 리스트 조회
    @GetMapping("/get-auths")
    public List<AdminAuthResDto> getAuths(@PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, AdminAuthsReqDto req) {
        return adminService.getAuths(pageable, req);
    }

    // 계정 조회
    @GetMapping("/get-auth")
    public AdminAuthResDto getAuth(AdminAuthReqDto req) {
        return adminService.getAuth(req);
    }

    // 계정 수정
    @PostMapping("/update-auth")
    public String updateAuth(@RequestBody(required = false) AdminAuthDataDto req) {
        System.out.println("req : " + req);
        return adminService.updateAuth(req);
    }

    // 유저 조회
    @GetMapping("/get-user")
    public AdminUserDataDto getUser(AdminUserReqDto req) {
        return adminService.getUser(req);
    }

    // 유저 수정
    @PostMapping("/update-user")
    public String updateUser(@RequestBody(required = false) AdminUserDataDto req) {
        return adminService.updateUser(req);
    }

    // 청원 수정
    @PostMapping("/update-petiton")
    public String updatePetition(@RequestBody(required = false) AdminUserDataDto req) {
        return adminService.updateUser(req);
    }
    

    // 게시글 수정
    @PostMapping("/update-post")
    public String updatePost(@RequestBody(required = false) AdminUserDataDto req) {
        return adminService.updateUser(req);
    }

    // 유저 계정 조회
    @GetMapping("/get-user-auth")
    public AdminUserAuthResDto getUserAuth(AdminUserAuthReqDto req) {
        return adminService.getUserAndAuth(req);
    }

    // 유저s 계정s 조회
    @GetMapping("/get-users-auths")
    public List<AdminUserAuthResDto> getUsersAuths(@PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, 
    AdminUsersAuthsReqDto req) {
        return adminService.getUsersAndAuths(pageable, req);
    }

}
