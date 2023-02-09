package kalo.main.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import kalo.main.controller.BasicException;
import kalo.main.domain.Notis;
import kalo.main.domain.Petition;
import kalo.main.domain.Post;
import kalo.main.domain.User;
import kalo.main.domain.dto.LikeDislikeResDto;
import kalo.main.domain.dto.TargetIdUserIdDto;
import kalo.main.domain.dto.petition.CreatePetitionReplyDto;
import kalo.main.domain.dto.petition.ReadPetitionDto;
import kalo.main.domain.dto.post.CreatePostReplyDto;
import kalo.main.domain.dto.user.JoinReqDto;
import kalo.main.domain.dto.user.UserAuthResDto;
import kalo.main.repository.NotisRepository;
import kalo.main.repository.PetitionRepository;
import kalo.main.repository.PostRepository;
import kalo.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class notisAop {

    
    private final UserRepository userRepository;
    private final NotisRepository notisRepository;
    private final PetitionRepository petitionRepository;
    private final PostRepository postRepository;
    
    Long kaloId = 112L;

    @AfterReturning(value = "execution(* kalo.main.service.UserService.createAuth(..))", returning = "object")
    public void joinNotis(JoinPoint joinPoint, Object object) {
        UserAuthResDto result = (UserAuthResDto) object;
        Long userId = result.getUserInfos().get(0).getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User kalo = userRepository.findById(kaloId).get();

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

    @AfterReturning(value = "execution(* kalo.main.service.PetitionService.supportingPetition(..))", returning = "object")
    public void supportMyPetitionNotis(JoinPoint joinPoint, Object object) {
        ReadPetitionDto result = (ReadPetitionDto) object;
        
        Long petitionWriterId = result.getWriter().getUserId();
        Long TargetId = result.getId();
        User petitionWriter = userRepository.findById(petitionWriterId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User kalo = userRepository.findById(kaloId).get();

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

    @Around("execution(* kalo.main.service.PetitionService.likePetition(..))")
    public LikeDislikeResDto likeMyPetitionNotis(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        TargetIdUserIdDto req = null;
        for (Object a : args) {
            req = (TargetIdUserIdDto) a;
        }
        
        LikeDislikeResDto result = (LikeDislikeResDto) joinPoint.proceed();

        if (result.getIsLike()) {
            User sender = userRepository.findById(req.getUserId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
            Petition petition = petitionRepository.findById(req.getTargetId()).orElseThrow(() -> new BasicException("청원을 찾을 수 없습니다."));
    
            Notis notis = Notis.builder()
            .image(null)
            .isCheck(false)
            .title(sender.getNickname() + "님이 내 청원을 좋아합니다.")
            .content(petition.getTitle())
            .isDisplay(true)
            .targetId(req.getTargetId())
            .sender(sender)
            .receiver(petition.getUser())
            .targetUrl("/community/petition/" + req.getTargetId())
            .target("petition")
            .build();

            notisRepository.save(notis);
        }

        return result;
    }

    @Around("execution(* kalo.main.service.PostService.likePost(..))")
    public LikeDislikeResDto likeMyPostNotis(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        TargetIdUserIdDto req = null;
        for (Object a : args) {
            req = (TargetIdUserIdDto) a;
        }

        LikeDislikeResDto result = (LikeDislikeResDto) joinPoint.proceed();

        if (result.getIsLike()) {
            User sender = userRepository.findById(req.getUserId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
            Post post = postRepository.findById(req.getTargetId()).orElseThrow(() -> new BasicException("게시글을 찾을 수 없습니다."));
    
            Notis notis = Notis.builder()
            .image(null)
            .isCheck(false)
            .title(sender.getNickname() + "님이 내 게시글을 좋아합니다.")
            .content(post.getTitle())
            .isDisplay(true)
            .targetId(req.getTargetId())
            .sender(sender)
            .receiver(post.getUser())
            .targetUrl("/community/post/" + req.getTargetId())
            .target("post")
            .build();
    
            notisRepository.save(notis);
        }

        return result;
    }
    
    @Around("execution(* kalo.main.service.PetitionService.createPetitionReply(..))")
    public Long writeMyPetitionReplyNotis(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        CreatePetitionReplyDto req = null;
        for (Object a : args) {
            req = (CreatePetitionReplyDto) a;
        }
        Long result = (Long) joinPoint.proceed();
        
        User sender = userRepository.findById(req.getUserId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User receiver = petitionRepository.findById(req.getPetitionId()).orElseThrow(() -> new BasicException("청원을 찾을 수 없습니다.")).getUser();

        Notis notis = Notis.builder()
        .image(null)
        .isCheck(false)
        .title(sender.getNickname() + "님이 내 청원에 댓글을 작성했습니다.")
        .content(req.getContent())
        .isDisplay(true)
        .targetId(req.getPetitionId())
        .sender(sender)
        .receiver(receiver)
        .targetUrl("/community/petition/" + req.getPetitionId())
        .target("petition")
        .build();

        notisRepository.save(notis);
        
        return result;
    }

    @Around("execution(* kalo.main.service.PostService.createPostReply(..))")
    public Long writeMyPostReplyNotis(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        CreatePostReplyDto req = null;
        for (Object a : args) {
            req = (CreatePostReplyDto) a;
        }
        Long result = (Long) joinPoint.proceed();

        User sender = userRepository.findById(req.getUserId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User receiver = postRepository.findById(req.getPostId()).orElseThrow(() -> new BasicException("게시글을 찾을 수 없습니다.")).getUser();

        Notis notis = Notis.builder()
        .image(null)
        .isCheck(false)
        .title(sender.getNickname() + "님이 내 게시글에 댓글을 작성했습니다.")
        .content(req.getContent())
        .isDisplay(true)
        .targetId(req.getPostId())
        .sender(sender)
        .receiver(receiver)
        .targetUrl("/community/post/" + req.getPostId())
        .target("post")
        .build();

        notisRepository.save(notis);

        return result;
    }

    @Around("execution(* kalo.main.service.PetitionService.likePetitionReply(..))")
    public LikeDislikeResDto likeMyPetitionReplyNotis(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        TargetIdUserIdDto req = null;
        for (Object a : args) {
            req = (TargetIdUserIdDto) a;
        }
        LikeDislikeResDto result = (LikeDislikeResDto) joinPoint.proceed();

        if (result.getIsLike()) {
            User sender = userRepository.findById(req.getUserId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
            Petition petition = petitionRepository.findById(req.getTargetId()).orElseThrow(() -> new BasicException("게시글을 찾을 수 없습니다."));
    
            Notis notis = Notis.builder()
            .image(null)
            .isCheck(false)
            .title(sender.getNickname() + "님이 내 댓글을 좋아합니다.")
            .content(petition.getContent())
            .isDisplay(true)
            .targetId(req.getTargetId())
            .sender(sender)
            .receiver(petition.getUser())
            .targetUrl("/community/petition/" + req.getTargetId())
            .target("petition")
            .build();
    
            notisRepository.save(notis);
        }

        return result;
    }

    @Around("execution(* kalo.main.service.PostService.likePostReply(..))")
    public LikeDislikeResDto likeMyPostReplyNotis(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        TargetIdUserIdDto req = null;
        for (Object a : args) {
            req = (TargetIdUserIdDto) a;
        }
        LikeDislikeResDto result = (LikeDislikeResDto) joinPoint.proceed();

        if (result.getIsLike()) {
            User sender = userRepository.findById(req.getUserId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
            Post post = postRepository.findById(req.getTargetId()).orElseThrow(() -> new BasicException("게시글을 찾을 수 없습니다."));
    
            Notis notis = Notis.builder()
            .image(null)
            .isCheck(false)
            .title(sender.getNickname() + "님이 내 댓글을 좋아합니다.")
            .content(post.getContent())
            .isDisplay(true)
            .targetId(req.getTargetId())
            .sender(sender)
            .receiver(post.getUser())
            .targetUrl("/community/post/" + req.getTargetId())
            .target("post")
            .build();
    
            notisRepository.save(notis);
        }

        return result;
    }
}
