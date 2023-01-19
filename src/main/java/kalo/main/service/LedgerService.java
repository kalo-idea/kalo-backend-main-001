package kalo.main.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kalo.main.controller.BasicException;
import kalo.main.domain.Ledger;
import kalo.main.domain.User;
import kalo.main.repository.LedgerRepository;
import kalo.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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
            throw new BasicException("이미 출석한 회원입니다.");
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
}
