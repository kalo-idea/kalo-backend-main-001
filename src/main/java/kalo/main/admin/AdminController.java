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
import kalo.main.admin.dto.AdminLedgerHistoryDto;
import kalo.main.admin.dto.AdminUserReqDto;
import kalo.main.admin.dto.AdminUserResDto;
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
    public AdminUserResDto getUser(AdminUserReqDto req) {
        return adminService.getUser(req);
    }

}
