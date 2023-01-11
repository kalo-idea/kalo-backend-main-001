package kalo.main.domain.dto.petition;

import lombok.Data;

@Data
public class CreatePetitionReplyDto {
    Long userId;
    Long petitionId;
    String content;
}
