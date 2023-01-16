package kalo.main.domain.dto.petition;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupportPetitionUserListDto {
    String nickname;
    Boolean isDeleted;
    LocalDateTime createdDate;

    @Builder
    @QueryProjection
    public SupportPetitionUserListDto(String nickname, Boolean isDeleted, LocalDateTime createdDate) {
        this.nickname = nickname;
        this.isDeleted = isDeleted;
        this.createdDate = createdDate;
    }
}
