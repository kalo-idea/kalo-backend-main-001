package kalo.main.admin.dto;

import java.time.LocalDateTime;

import kalo.main.domain.Ledger;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminLedgerHistoryDto {
    
    LocalDateTime time;
    Long amount;
    String type;
    Boolean deleted;

    public AdminLedgerHistoryDto(Ledger ledger) {
        this.time = ledger.getCreatedDate();
        this.amount = ledger.getAmount();
        this.type = ledger.getType();
        this.deleted = ledger.getDeleted();
    }
}
