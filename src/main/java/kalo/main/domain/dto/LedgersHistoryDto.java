package kalo.main.domain.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LedgersHistoryDto {

    String type;
    Long amount;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


    @QueryProjection
    @Builder
    public LedgersHistoryDto(String type, Long amount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
