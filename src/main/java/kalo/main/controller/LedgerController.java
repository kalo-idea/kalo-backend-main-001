package kalo.main.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import kalo.main.domain.dto.OnlyIdDto;
import kalo.main.service.LedgerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;
    
    // 출석체크 여부
    @GetMapping("/is-attend")
    public Boolean isAttend(Long userId) {
        return ledgerService.isAttend(userId);
    }

    // 출석체크
    @GetMapping("/attend")
    public Long attend(@RequestBody OnlyIdDto onlyIdDto) {
        return ledgerService.attend(onlyIdDto);
    }

    // 이번달 출석일자
    @GetMapping("/get-attend")
    public List<LocalDate> getAttend(Long userId, int year, int month) {
        return ledgerService.getAttend(userId, year, month);
    }

    @PostMapping("/get-point")
    public Long getPoint(@RequestBody OnlyIdDto onlyIdDto) {
        return ledgerService.getPoint(onlyIdDto);
    }
}
