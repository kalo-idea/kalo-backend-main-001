package kalo.main.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.controller.BasicException;
import kalo.main.domain.Notis;
import kalo.main.domain.Petition;
import kalo.main.domain.Post;
import kalo.main.domain.User;
import kalo.main.domain.dto.NotisResDto;
import kalo.main.repository.NotisRepository;
import kalo.main.repository.PetitionRepository;
import kalo.main.repository.PostRepository;
import kalo.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotisService {
    
    private final UserRepository userRepository;
    private final NotisRepository notisRepository;
    private final PetitionRepository petitionRepository;
    private final PostRepository postRepository;

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

    public void joinNotis(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User kalo = userRepository.findById(112L).get();

        Notis notis = Notis.builder()
        .image("kalo")
        .isCheck(false)
        .title(null)
        .content("회원가입을 축하합니다. 당신의 목소리를 들려주세요!")
        .isDisplay(true)
        .targetId(userId)
        .sender(kalo)
        .receiver(user)
        .targetUrl(null)
        .target("user")
        .build();

        notisRepository.save(notis);
    }

    public void supportMyPetitionNotis(Long petitionWriterId, Long TargetId) {
        User petitionWriter = userRepository.findById(petitionWriterId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User kalo = userRepository.findById(112L).get();

        Notis notis = Notis.builder()
        .image(null)
        .isCheck(false)
        .title("누군가 내 청원에 참여했어요.")
        .content(null)
        .isDisplay(true)
        .targetId(TargetId)
        .sender(kalo)
        .receiver(petitionWriter)
        .targetUrl("/community/petition/" + TargetId)
        .target("petition")
        .build();

        notisRepository.save(notis);
    }

    public void likeMyPetitionNotis(Long senderId, Long receiverId, Long TargetId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        Petition petition = petitionRepository.findById(TargetId).orElseThrow(() -> new BasicException("청원을 찾을 수 없습니다."));

        Notis notis = Notis.builder()
        .image(null)
        .isCheck(false)
        .title(sender.getNickname() + "님이 내 청원을 좋아합니다.")
        .content(petition.getTitle())
        .isDisplay(true)
        .targetId(TargetId)
        .sender(sender)
        .receiver(receiver)
        .targetUrl("/community/petition/" + TargetId)
        .target("petition")
        .build();

        notisRepository.save(notis);
    }

    public void likeMyPostNotis(Long senderId, Long receiverId, Long TargetId) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        Post post = postRepository.findById(TargetId).orElseThrow(() -> new BasicException("게시글을 찾을 수 없습니다."));

        Notis notis = Notis.builder()
        .image(null)
        .isCheck(false)
        .title(sender.getNickname() + "님이 내 청원을 좋아합니다.")
        .content(post.getTitle())
        .isDisplay(true)
        .targetId(TargetId)
        .sender(sender)
        .receiver(receiver)
        .targetUrl("/community/post/" + TargetId)
        .target("post")
        .build();

        notisRepository.save(notis);
    }

    public void writeMyPetitionReplyNotis(Long senderId, Long receiverId, Long TargetId, String content) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));

        Notis notis = Notis.builder()
        .image(null)
        .isCheck(false)
        .title(sender.getNickname() + "님이 내 청원에 댓글을 작성했습니다.")
        .content(content)
        .isDisplay(true)
        .targetId(TargetId)
        .sender(sender)
        .receiver(receiver)
        .targetUrl("/community/petition/" + TargetId)
        .target("petition")
        .build();

        notisRepository.save(notis);
    }

    public void writeMyPostReplyNotis(Long senderId, Long receiverId, Long TargetId, String content) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));

        Notis notis = Notis.builder()
        .image(null)
        .isCheck(false)
        .title(sender.getNickname() + "님이 내 게시글에 댓글을 작성했습니다.")
        .content(content)
        .isDisplay(true)
        .targetId(TargetId)
        .sender(sender)
        .receiver(receiver)
        .targetUrl("/community/post/" + TargetId)
        .target("post")
        .build();

        notisRepository.save(notis);
    }

    public void likeMyPetitionReplyNotis(Long senderId, Long receiverId, Long TargetId, String content) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));

        Notis notis = Notis.builder()
        .image(null)
        .isCheck(false)
        .title(sender.getNickname() + "님이 내 청원을 좋아합니다.")
        .content(content)
        .isDisplay(true)
        .targetId(TargetId)
        .sender(sender)
        .receiver(receiver)
        .targetUrl("/community/petition/" + TargetId)
        .target("petition")
        .build();

        notisRepository.save(notis);
    }

    public void likeMyPostReplyNotis(Long senderId, Long receiverId, Long TargetId, String content) {
        User sender = userRepository.findById(senderId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));

        Notis notis = Notis.builder()
        .image(null)
        .isCheck(false)
        .title(sender.getNickname() + "님이 내 게시글을 좋아합니다.")
        .content(content)
        .isDisplay(true)
        .targetId(TargetId)
        .sender(sender)
        .receiver(receiver)
        .targetUrl("/community/post/" + TargetId)
        .target("post")
        .build();

        notisRepository.save(notis);
    }
    
}
