package kalo.main.domain.dto;

import kalo.main.domain.Notis;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotisResDto {
    Long id;
    String image;
    String title;
    String content;
    Boolean isCheck;
    Boolean isDisplay;
    Long senderId;
    Long receiverId;
    Long targetId;
    String targetUrl;
    String target;

    public NotisResDto(Notis notis) {
        this.id = notis.getId();
        this.image = notis.getImage();
        this.title = notis.getTitle();
        this.content = notis.getContent();
        this.isCheck = notis.getIsCheck();
        this.isDisplay = notis.getIsDisplay();
        this.senderId = notis.getSender().getId();
        this.receiverId = notis.getReceiver().getId();
        this.targetId = notis.getTargetId();
        this.targetUrl = notis.getTargetUrl();
        this.target = notis.getTarget();
    }
}
