package kalo.main.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.controller.BasicException;
import kalo.main.domain.Ledger;
import kalo.main.domain.User;
import kalo.main.domain.dto.LedgerHistoryDto;
import kalo.main.domain.dto.OnlyIdDto;
import kalo.main.repository.LedgerRepository;
import kalo.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LedgerService {
    private final LedgerRepository ledgerRepository;
    private final UserRepository userRepository;


    // 금일 출석체크 여부
    public Boolean isAttend(Long userId) {
        return !ledgerRepository.isAttend(userId).isEmpty();
    }

    // 출석
    public Long attend(Long userId) {
        if (!isAttend(userId)) {
            User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("없는 회원입니다."));

            Ledger ledger = Ledger.builder().user(user).type("attendence").amount(500L).build();

            ledgerRepository.save(ledger);
            return ledger.getAmount();
        }
        else {
            return 0L;
        }
    }

    // 이번달 출석일자
    public List<LocalDate> getAttend(Long userId, int year, int month) {

        List<LocalDateTime> lists = ledgerRepository.getAttend(userId, year, month);
        List<LocalDate> result = new ArrayList<LocalDate>();
        for (LocalDateTime localDateTime : lists) {
            result.add(localDateTime.toLocalDate());
        }
        return result;
    } 

    // 회원 조회용 거래내역
    public List<LedgerHistoryDto> getLedgersHistory(Long userId) {        
        List<Ledger> ledger = ledgerRepository.findByUserIdAndDeletedOrderByCreatedDateDesc(userId, false).get();
        List<LedgerHistoryDto> history = new ArrayList<LedgerHistoryDto>();
        for (Ledger led : ledger) {
            history.add(new LedgerHistoryDto(led));
        }

        return history;
    }

    public Long getPoint(Long userId) {
        Long ledgers = ledgerRepository.getSumUserLedger(userId);
        return ledgers;
    }
}
