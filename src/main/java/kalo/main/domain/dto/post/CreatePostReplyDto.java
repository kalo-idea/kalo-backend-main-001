package kalo.main.domain.dto.post;

import lombok.Data;

@Data
public class CreatePostReplyDto {
    Long userId;
    Long postId;
    String content;
}
