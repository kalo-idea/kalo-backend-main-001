package kalo.main.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import kalo.main.controller.BasicException;
import kalo.main.domain.LikePetition;
import kalo.main.domain.LikePetitionReply;
import kalo.main.domain.LikePost;
import kalo.main.domain.LikePostReply;
import kalo.main.domain.Noti;
import kalo.main.domain.Petition;
import kalo.main.domain.PetitionReply;
import kalo.main.domain.Post;
import kalo.main.domain.PostReply;
import kalo.main.domain.User;
import kalo.main.domain.dto.LikeDislikeResDto;
import kalo.main.domain.dto.TargetIdUserIdDto;
import kalo.main.domain.dto.petition.CreatePetitionReplyDto;
import kalo.main.domain.dto.petition.ReadPetitionDto;
import kalo.main.domain.dto.post.CreatePostReplyDto;
import kalo.main.domain.dto.user.UserAuthResDto;
import kalo.main.repository.LikePetitionReplyRepository;
import kalo.main.repository.LikePetitionRepository;
import kalo.main.repository.LikePostReplyRepository;
import kalo.main.repository.LikePostRepository;
import kalo.main.repository.NotiRepository;
import kalo.main.repository.PetitionReplyRepository;
import kalo.main.repository.PetitionRepository;
import kalo.main.repository.PostReplyRepository;
import kalo.main.repository.PostRepository;
import kalo.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class notisAop {

    
    private final UserRepository userRepository;
    private final NotiRepository notisRepository;
    private final PetitionRepository petitionRepository;
    private final PostRepository postRepository;
    private final PetitionReplyRepository petitionReplyRepository;
    private final PostReplyRepository postReplyRepository;
    private final LikePetitionRepository likePetitionRepository;
    private final LikePostRepository likePostRepository;
    private final LikePetitionReplyRepository likePetitionReplyRepository;
    private final LikePostReplyRepository likePostReplyRepository;
    
    Long kaloId = 112L;

    @AfterReturning(value = "execution(* kalo.main.service.UserService.createAuth(..))", returning = "object")
    public void joinNotis(JoinPoint joinPoint, Object object) {
        UserAuthResDto result = (UserAuthResDto) object;
        Long userId = result.getUserInfos().get(0).getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        User kalo = userRepository.findById(kaloId).get();

        Noti notis = Noti.builder()
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

    @Around("execution(* kalo.main.service.PetitionService.supportingPetition(..))")
    public ReadPetitionDto supportMyPetitionNotis(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        TargetIdUserIdDto req = null;
        for (Object a : args) {
            req = (TargetIdUserIdDto) a;
        }

        ReadPetitionDto result = (ReadPetitionDto) joinPoint.proceed();
        Long petitionWriterId = result.getWriter().getUserId();
        if (petitionWriterId != null && req.getUserId() != petitionWriterId) {
            Long TargetId = result.getId();
            User petitionWriter = userRepository.findById(petitionWriterId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
            User kalo = userRepository.findById(kaloId).get();
    
            Noti notis = Noti.builder()
            .image(null)
            .isCheck(false)
            .title("누군가 내 청원에 참여했어요.")
            .content(null)
            .isDisplay(true)
            .targetId(TargetId)
            .sender(kalo)
            .receiver(petitionWriter)
            .targetUrl("/community/view/petition/" + TargetId)
            .target("petition")
            .build();
    
            notisRepository.save(notis);
        }

        return result;
    }

    @Around("execution(* kalo.main.service.PetitionService.likePetition(..))")
    public LikeDislikeResDto likeMyPetitionNotis(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        TargetIdUserIdDto req = null;
        for (Object a : args) {
            req = (TargetIdUserIdDto) a;
        }
        
        LikeDislikeResDto result = (LikeDislikeResDto) joinPoint.proceed();
        User sender = userRepository.findById(req.getUserId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        Petition petition = petitionRepository.findById(req.getTargetId()).orElseThrow(() -> new BasicException("청원을 찾을 수 없습니다."));
        
        if (result.getIsLike()) {
            LikePetition likePetition = likePetitionRepository.findByPetitionIdAndUserIdAndDeleted(req.getTargetId(), req.getUserId(), false).get();

            if (petition.getUser().getId() != null && req.getUserId() != petition.getUser().getId() && likePetition.getCreatedDate() == likePetition.getLastModifiedDate()) {

                Noti notis = Noti.builder()
                .image(null)
                .isCheck(false)
                .title(sender.getNickname() + "님이 내 청원을 좋아합니다.")
                .content(petition.getTitle())
                .isDisplay(true)
                .targetId(req.getTargetId())
                .sender(sender)
                .receiver(petition.getUser())
                .targetUrl("/community/view/petition/" + req.getTargetId())
                .target("petition")
                .build();
    
                notisRepository.save(notis);
            }
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
        User sender = userRepository.findById(req.getUserId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        Post post = postRepository.findById(req.getTargetId()).orElseThrow(() -> new BasicException("게시글을 찾을 수 없습니다."));

        if (result.getIsLike()) {
            LikePost likePost = likePostRepository.findByPostIdAndUserIdAndDeleted(req.getTargetId(), req.getUserId(), false).get();

            if (post.getUser().getId() != null && sender.getId() != post.getUser().getId() && likePost.getCreatedDate() == likePost.getLastModifiedDate()) {
                Noti notis = Noti.builder()
                .image(null)
                .isCheck(false)
                .title(sender.getNickname() + "님이 내 게시글을 좋아합니다.")
                .content(post.getTitle())
                .isDisplay(true)
                .targetId(req.getTargetId())
                .sender(sender)
                .receiver(post.getUser())
                .targetUrl("/community/view/post/" + req.getTargetId())
                .target("post")
                .build();
        
                notisRepository.save(notis);
            }
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
        Petition petition = petitionRepository.findById(req.getPetitionId()).orElseThrow(() -> new BasicException("청원을 찾을 수 없습니다."));
        User receiver = petition.getUser();

        String content = req.getContent().length() < 100? req.getContent():req.getContent().substring(0, 100);

        if (receiver.getId() != null && receiver.getId() != sender.getId()) {

            Noti notis = Noti.builder()
            .image(null)
            .isCheck(false)
            .title(sender.getNickname() + "님이 내 청원에 댓글을 작성했습니다.")
            .content(content)
            .isDisplay(true)
            .targetId(req.getPetitionId())
            .sender(sender)
            .receiver(receiver)
            .targetUrl("/community/view/petition/" + req.getPetitionId())
            .target("petition")
            .build();

            notisRepository.save(notis);
        }
        
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

        String content = req.getContent().length() < 100? req.getContent():req.getContent().substring(0, 100);

        if (receiver.getId() != null && sender.getId() != receiver.getId()) {
            Noti notis = Noti.builder()
            .image(null)
            .isCheck(false)
            .title(sender.getNickname() + "님이 내 게시글에 댓글을 작성했습니다.")
            .content(content)
            .isDisplay(true)
            .targetId(req.getPostId())
            .sender(sender)
            .receiver(receiver)
            .targetUrl("/community/view/post/" + req.getPostId())
            .target("post")
            .build();
    
            notisRepository.save(notis);
        }

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
        User sender = userRepository.findById(req.getUserId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        PetitionReply petitionReply = petitionReplyRepository.findById(req.getTargetId()).orElseThrow(() -> new BasicException("댓글을 찾을 수 없습니다."));

        String content = petitionReply.getContent().length() < 100? petitionReply.getContent():petitionReply.getContent().substring(0, 100);

        if (result.getIsLike()) {
            LikePetitionReply reply = likePetitionReplyRepository.findByPetitionReplyIdAndUserIdAndDeleted(req.getTargetId(), req.getUserId(), false).get();

            if (result.getIsLike() && petitionReply.getUser().getId() != null && sender.getId() != petitionReply.getUser().getId() && reply.getCreatedDate() == reply.getLastModifiedDate()) {
                Noti notis = Noti.builder()
                .image(null)
                .isCheck(false)
                .title(sender.getNickname() + "님이 내 댓글을 좋아합니다.")
                .content(content)
                .isDisplay(true)
                .targetId(petitionReply.getPetition().getId())
                .sender(sender)
                .receiver(petitionReply.getUser())
                .targetUrl("/community/view/petition/" + petitionReply.getPetition().getId())
                .target("petition")
                .build();
        
                notisRepository.save(notis);
            }
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
        User sender = userRepository.findById(req.getUserId()).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        PostReply postReply = postReplyRepository.findById(req.getTargetId()).orElseThrow(() -> new BasicException("댓글을 찾을 수 없습니다."));
        String content = postReply.getContent().length() < 100? postReply.getContent():postReply.getContent().substring(0, 100);

        if (result.getIsLike()) {
            LikePostReply reply = likePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(req.getTargetId(), req.getUserId(), false).get();

            if (postReply.getUser().getId() != null && sender.getId() != postReply.getUser().getId() && reply.getCreatedDate() == reply.getLastModifiedDate()) {
                Noti notis = Noti.builder()
                .image(null)
                .isCheck(false)
                .title(sender.getNickname() + "님이 내 댓글을 좋아합니다.")
                .content(content)
                .isDisplay(true)
                .targetId(postReply.getPost().getId())
                .sender(sender)
                .receiver(postReply.getUser())
                .targetUrl("/community/view/post/" + postReply.getPost().getId())
                .target("post")
                .build();
        
                notisRepository.save(notis);
            }
        }

        return result;
    }
}
