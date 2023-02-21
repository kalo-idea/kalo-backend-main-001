package kalo.main.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kalo.main.domain.dto.NotisResDto;
import kalo.main.domain.dto.OnlyIdDto;
import kalo.main.service.NotiService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotisController {
    private final NotiService notisService;

    @GetMapping("/get-my-notis")
    public List<NotisResDto> getMyNotis(@PageableDefault(size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, Long userId) {
        return notisService.getMyNotis(pageable, userId);
    }

    @GetMapping("/count-my-notis")
    public Long countMyNotis(Long userId) {
        return notisService.countMyNotis(userId);
    }

    @PostMapping("/check-noti")
    public Boolean notiCheck(@RequestBody OnlyIdDto req) {
        return notisService.notiCheck(req.getId());
    }

    @PostMapping("/undisplay-noti")
    public Boolean notiUndisplay(@RequestBody OnlyIdDto req) {
        return notisService.notiUndisplay(req.getId());
    }
}