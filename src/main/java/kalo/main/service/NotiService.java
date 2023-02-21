package kalo.main.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.controller.BasicException;
import kalo.main.domain.Noti;
import kalo.main.domain.dto.NotisResDto;
import kalo.main.repository.NotiRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotiService {
    
    private final NotiRepository notiRepository;
    
    Long kaloId = 112L;

    public List<NotisResDto> getMyNotis(Pageable pageable, Long userId) {
        List<Noti> notis = notiRepository.findNotisByReceiverIdAndDeletedAndIsDisplay(pageable, userId, false, true);
        
        List<NotisResDto> res = new ArrayList<NotisResDto>();
        for (Noti noti : notis) {
            res.add(new NotisResDto(noti));
        }
        return res;
    }

    public Long countMyNotis(Long userId) {
        return notiRepository.countNotisByReceiverIdAndDeletedAndIsDisplayAndIsCheck(userId, false, true, false);
    }

    public Boolean notiCheck(Long noticeId) {
        Noti notis = notiRepository.findById(noticeId).orElseThrow(() -> new BasicException("알림을 찾을 수 없습니다."));
        notis.setIsCheck(true);
        
        return true;
    }

    public Boolean notiUndisplay(Long noticeId) {
        Noti notis = notiRepository.findById(noticeId).orElseThrow(() -> new BasicException("알림을 찾을 수 없습니다."));
        notis.setIsDisplay(false);
        
        return true;
    }
    
}
