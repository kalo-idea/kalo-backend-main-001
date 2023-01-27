package kalo.main.domain.dto;

import java.time.LocalDateTime;

import kalo.main.domain.Ledger;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LedgerHistoryDto {
    LocalDateTime time;
    Long amount;
    String type;

    public LedgerHistoryDto(Ledger ledger) {
        this.time = ledger.getCreatedDate();
        this.amount = ledger.getAmount();
        this.type = ledger.getType();
    }
}
