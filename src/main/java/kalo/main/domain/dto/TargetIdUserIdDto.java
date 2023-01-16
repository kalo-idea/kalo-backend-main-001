package kalo.main.domain.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TargetIdUserIdDto {
    @NotNull
    Long targetId;
    @NotNull
    Long userId;
}
