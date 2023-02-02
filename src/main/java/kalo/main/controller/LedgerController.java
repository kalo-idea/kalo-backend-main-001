package kalo.main.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kalo.main.domain.dto.OnlyIdDto;
import kalo.main.service.LedgerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;
    
    // 출석체크 여부
    @GetMapping("/is-attend")
    public Boolean isAttend(Long id) {
        return ledgerService.isAttend(id);
    }

    // 출석체크
    @PostMapping("/attend")
    public Long attend(@RequestBody OnlyIdDto req) {
        return ledgerService.attend(req.getId());
    }

    // 이번달 출석일자
    @GetMapping("/get-attend")
    public List<LocalDate> getAttend(Long id, int year, int month) {
        return ledgerService.getAttend(id, year, month);
    }

    @PostMapping("/get-point")
    public Long getPoint(@RequestBody OnlyIdDto req) {
        return ledgerService.getPoint(req.getId());
    }
}
