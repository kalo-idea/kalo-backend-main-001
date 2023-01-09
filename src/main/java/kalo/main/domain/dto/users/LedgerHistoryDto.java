package kalo.main.domain.dto.users;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LedgerHistoryDto {

    String type;
    Long amount;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


    @QueryProjection
    @Builder
    public LedgerHistoryDto(String type, Long amount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
