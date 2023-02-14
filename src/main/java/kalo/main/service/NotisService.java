package kalo.main.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.controller.BasicException;
import kalo.main.domain.Notis;
import kalo.main.domain.dto.NotisResDto;
import kalo.main.repository.NotisRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotisService {
    
    private final NotisRepository notisRepository;
    
    Long kaloId = 112L;

    public List<NotisResDto> getMyNotis(Pageable pageable, Long userId) {
        List<Notis> notis = notisRepository.findNotisByReceiverIdAndDeletedAndIsDisplay(pageable, userId, false, true);
        
        List<NotisResDto> res = new ArrayList<NotisResDto>();
        for (Notis noti : notis) {
            res.add(new NotisResDto(noti));
        }
        return res;
    }

    public Long checkMyNotis(Long userId) {
        return notisRepository.countNotisByReceiverIdAndDeletedAndIsDisplayAndIsCheck(userId, false, true, false);
    }

    public Boolean notisCheck(Long noticeId) {
        Notis notis = notisRepository.findById(noticeId).orElseThrow(() -> new BasicException("알림을 찾을 수 없습니다."));
        notis.setIsCheck(true);
        
        return true;
    }

    public Boolean notisUndisplay(Long noticeId) {
        Notis notis = notisRepository.findById(noticeId).orElseThrow(() -> new BasicException("알림을 찾을 수 없습니다."));
        notis.setIsDisplay(false);
        
        return true;
    }
    
}
