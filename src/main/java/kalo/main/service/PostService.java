package kalo.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.controller.BasicException;
import kalo.main.domain.Hashtag;
import kalo.main.domain.Post;
import kalo.main.domain.PostHashtag;
import kalo.main.domain.PostReply;
import kalo.main.domain.User;
import kalo.main.domain.dto.ReplyDto;
import kalo.main.domain.dto.post.CreatePostDto;
import kalo.main.domain.dto.post.CreatePostReplyDto;
import kalo.main.domain.dto.post.PostCondDto;
import kalo.main.domain.dto.post.ReadPostDto;
import kalo.main.domain.dto.post.ReadPostsDto;
import kalo.main.repository.DislikePostReplyRepository;
import kalo.main.repository.DislikePostRepository;
import kalo.main.repository.HashtagRepository;
import kalo.main.repository.LikePostReplyRepository;
import kalo.main.repository.LikePostRepository;
import kalo.main.repository.PostHashtagRepository;
import kalo.main.repository.PostReplyRepository;
import kalo.main.repository.PostRepository;
import kalo.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postsHashtagRepository;
    private final LikePostRepository likePostRepository;
    private final DislikePostRepository dislikePostRepository;
    private final PostReplyRepository postReplyRepository;
    private final LikePostReplyRepository likePostReplyRepository;
    private final DislikePostReplyRepository dislikePostReplyRepository;

    // 게시글 생성
    public Long createPost(CreatePostDto createPostsDto) {

        Post posts = Post.builder()
        .title(createPostsDto.getTitle())
        .content(createPostsDto.getContent())
        .photos(createPostsDto.getPhotos())
        .viewCount(0L)
        .user(userRepository.findById(createPostsDto.getUserId()).get())
        .replyCount(0L)
        .likeCount(0L)
        .dislikeCount(0L)
        .topic(createPostsDto.getTopic())
        .addressName(createPostsDto.getAddressName())
        .region1depthName(createPostsDto.getRegion1depthName())
        .region2depthName(createPostsDto.getRegion2depthName())
        .region3depthName(createPostsDto.getRegion3depthName())
        .latitude(createPostsDto.getLatitude())
        .longitude(createPostsDto.getLongitude())
        .build();

        Post resultPost = postRepository.save(posts);

        List<String> hashtags = createPostsDto.getHashtags();
        for (String hashtag : hashtags) {
            try {
                // hashtag find
                PostHashtag postsHashtags = PostHashtag.builder()
                    .post(resultPost)
                    .hashtag(hashtagRepository.findByWordAndDeleted(hashtag, false).orElseThrow())
                    .build();
                postsHashtagRepository.save(postsHashtags);
            } catch(NoSuchElementException e) {
                // hashtag make
                Hashtag makeHashtag = new Hashtag(hashtag);
                hashtagRepository.save(makeHashtag);
                PostHashtag postsHashtags = PostHashtag.builder()
                    .post(resultPost)
                    .hashtag(makeHashtag)
                    .build();
                postsHashtagRepository.save(postsHashtags);
            }
        }

        return resultPost.getId();
    }

    public ReadPostDto readPost(Long postId, Long viewerId) {
        Post post = postRepository.findById(postId).get();

        Boolean isLike = false;
        Boolean isDislike = false;
        if (viewerId != null) {
            isLike = likePostRepository.findByPostIdAndUserIdAndDeleted(postId, viewerId, false).isPresent();
            isDislike = dislikePostRepository.findByPostIdAndUserIdAndDeleted(postId, viewerId, false).isPresent();
        }

        if (post.getDeleted()) {
            throw new BasicException("삭제된 게시글입니다.");
        }

        User writer = userRepository.findById(post.getUser().getId()).get();
        List<Hashtag> hashs = hashtagRepository.findPostHashtags(postId);
        List<String> hashtags = new ArrayList();

        for (Hashtag hash : hashs) {
            hashtags.add(hash.getWord());
        }
        
        post.setViewCount(post.getViewCount() + 1);

        if (writer.getDeleted()) {
            ReadPostDto.builder()
            .title(post.getTitle())
            .createdDate(post.getCreatedDate())
            .content(post.getContent())
            .hashtags(hashtags)
            .photos(post.getPhotos())
            .likeCount(post.getLikeCount())
            .isLike(isLike)
            .dislikeCount(post.getDislikeCount())
            .isDislike(isDislike)
            .replyCount(post.getReplyCount())
            .topic(post.getTopic())
            .region1depthName(post.getRegion1depthName())
            .region2depthName(post.getRegion2depthName())
            .build();
        }

        return ReadPostDto.builder()
        .userId(writer.getId())
        .nickname(writer.getNickname())
        .profileSrc(writer.getProfileSrc())
        .title(post.getTitle())
        .createdDate(post.getCreatedDate())
        .content(post.getContent())
        .hashtags(hashtags)
        .photos(post.getPhotos())
        .likeCount(post.getLikeCount())
        .isLike(isLike)
        .dislikeCount(post.getDislikeCount())
        .isDislike(isDislike)
        .replyCount(post.getReplyCount())
        .topic(post.getTopic())
        .region1depthName(post.getRegion1depthName())
        .region2depthName(post.getRegion2depthName())
        .build();
    }

    // 청원 댓글 추가
    public Long createPostReply(CreatePostReplyDto createPostReplyDto) {
        PostReply postReply = PostReply.builder()
        .user(userRepository.findById(createPostReplyDto.getUserId()).orElseThrow(() -> new BasicException("없는 회원입니다.")))
        .content(createPostReplyDto.getContent())
        .post(postRepository.findById(createPostReplyDto.getPostId()).orElseThrow(() -> new BasicException("없는 게시글입니다.")))
        .likeCount(0L)
        .dislikeCount(0L)
        .build();

        PostReply result = postReplyRepository.save(postReply);

        return result.getId();
    }

    // 청원 댓글 조회
    public List<ReplyDto> readPostReply(Long petitionId, Long viewerId, Pageable pageable) {

        List<PostReply> replys = postReplyRepository.findByPostIdAndDeleted(petitionId, pageable, false);

        List<ReplyDto> result = new ArrayList();

        for (PostReply reply : replys) {
            Boolean isLike = false;
            Boolean isDislike = false;
            if (viewerId != null) {
                isLike = likePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(reply.getId(), viewerId, false).isPresent();
                isDislike = dislikePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(reply.getId(), viewerId, false).isPresent();
            }
            // 댓글 작성자 탈퇴의 경우
            if (reply.getUser().getDeleted()) {
                ReplyDto commentDto = new ReplyDto().builder()
                .commentId(reply.getId())
                .isLike(isLike)
                .likeCount(reply.getLikeCount())
                .isDislike(isDislike)
                .dislikeCount(reply.getDislikeCount())
                .content(reply.getContent())
                .build();
    
                result.add(commentDto);
            }
            if (!reply.getUser().getDeleted()) {
                ReplyDto commentDto = new ReplyDto().builder()
                .commentId(reply.getId())
                .userId(reply.getUser().getId())
                .nickname(reply.getUser().getNickname())
                .profileSrc(reply.getUser().getProfileSrc())
                .isLike(isLike)
                .likeCount(reply.getLikeCount())
                .isDislike(isDislike)
                .dislikeCount(reply.getDislikeCount())
                .content(reply.getContent())
                .build();
    
                result.add(commentDto);
            }
        }
        return result;
    }

    // 게시글 리스트 조회
    // public List<ReadPostsDto> readPosts(Pageable pageable, PostCondDto cond) {

    // }

}