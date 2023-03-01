package kalo.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kalo.main.controller.BasicException;
import kalo.main.domain.DislikePost;
import kalo.main.domain.DislikePostReply;
import kalo.main.domain.Hashtag;
import kalo.main.domain.LikePost;
import kalo.main.domain.LikePostReply;
import kalo.main.domain.Media;
import kalo.main.domain.MediaPost;
import kalo.main.domain.Post;
import kalo.main.domain.PostHashtag;
import kalo.main.domain.PostReply;
import kalo.main.domain.User;
import kalo.main.domain.dto.LikeDislikeResDto;
import kalo.main.domain.dto.ReplyDto;
import kalo.main.domain.dto.SimpleDeletedWriterDto;
import kalo.main.domain.dto.SimpleWriterDto;
import kalo.main.domain.dto.TargetIdUserIdDto;
import kalo.main.domain.dto.post.CreatePostDto;
import kalo.main.domain.dto.post.CreatePostReplyDto;
import kalo.main.domain.dto.post.PostCondDto;
import kalo.main.domain.dto.post.ReadPostDto;
import kalo.main.domain.dto.post.ReadPostsDto;
import kalo.main.domain.dto.post.ReadSimplePostDto;
import kalo.main.repository.DislikePostReplyRepository;
import kalo.main.repository.DislikePostRepository;
import kalo.main.repository.HashtagRepository;
import kalo.main.repository.LikePostReplyRepository;
import kalo.main.repository.LikePostRepository;
import kalo.main.repository.MediaPostRepository;
import kalo.main.repository.MediaRepository;
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
    private final PostHashtagRepository postHashtagRepository;
    private final LikePostRepository likePostRepository;
    private final DislikePostRepository dislikePostRepository;
    private final PostReplyRepository postReplyRepository;
    private final LikePostReplyRepository likePostReplyRepository;
    private final DislikePostReplyRepository dislikePostReplyRepository;
    private final MediaRepository mediaRepository;
    private final MediaPostRepository mediaPostRepository;

    // 게시글 생성
    public Long createPost(CreatePostDto createPostsDto) {
        User user = userRepository.findByIdAndDeleted(createPostsDto.getWriterId(), false).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));

        Post post = Post.builder()
        .title(createPostsDto.getTitle())
        .content(createPostsDto.getContent())
        .viewCount(0L)
        .user(user)
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

        Post resultPost = postRepository.save(post);

        List<String> hashtags = createPostsDto.getHashtags();
        for (String hashtag : hashtags) {
            try {
                // hashtag find
                PostHashtag postsHashtags = PostHashtag.builder()
                    .post(resultPost)
                    .hashtag(hashtagRepository.findByWordAndDeleted(hashtag, false).orElseThrow())
                    .build();
                postHashtagRepository.save(postsHashtags);
            } catch(NoSuchElementException e) {
                // hashtag make
                Hashtag makeHashtag = new Hashtag(hashtag);
                hashtagRepository.save(makeHashtag);
                PostHashtag postsHashtags = PostHashtag.builder()
                    .post(resultPost)
                    .hashtag(makeHashtag)
                    .build();
                postHashtagRepository.save(postsHashtags);
            }
        }

        List<String> media = createPostsDto.getMedium();
        for (String fileName : media) {
            Media medium = new Media(fileName);
            mediaRepository.save(medium);
            MediaPost mediaPost = MediaPost.builder()
                .media(medium)
                .post(post)
                .build();
            mediaPostRepository.save(mediaPost);
        }

        return resultPost.getId();
    }

    // 게시글 단건 조회
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

        List<Hashtag> hashtags = hashtagRepository.findPostHashtags(postId);
        List<String> hashtags_res = new ArrayList<String>();

        for (Hashtag hashtag : hashtags) {
            hashtags_res.add(hashtag.getWord());
        }

        List<Media> media = mediaRepository.findPostMedia(postId);
        List<String> media_res = new ArrayList<String>();

        for (Media medium : media) {
            media_res.add(medium.getFileName());
        }
        
        post.setViewCount(post.getViewCount() + 1);

        User user = userRepository.findById(post.getUser().getId()).get();
        SimpleWriterDto writer = !user.getDeleted() ? new SimpleWriterDto(user) : new SimpleDeletedWriterDto();

        return ReadPostDto.builder()
        .id(post.getId())
        .writer(writer)
        .title(post.getTitle())
        .createdDate(post.getCreatedDate())
        .content(post.getContent())
        .hashtags(hashtags_res)
        .medium(media_res)
        .likeCount(post.getLikeCount())
        .isLike(isLike)
        .dislikeCount(post.getDislikeCount())
        .isDislike(isDislike)
        .replyCount(post.getReplyCount())
        .topic(post.getTopic())
        .region1depthName(post.getRegion1depthName())
        .region2depthName(post.getRegion2depthName())
        .longitude(post.getLongitude())
        .latitude(post.getLatitude())
        .build();
    }

    // 게시글 댓글 추가
    public Long createPostReply(CreatePostReplyDto createPostReplyDto) {
        Post post = postRepository.findById(createPostReplyDto.getPostId()).orElseThrow(() -> new BasicException("없는 게시글입니다."));
        post.setReplyCount(post.getReplyCount() + 1);
        
        PostReply postReply = PostReply.builder()
        .user(userRepository.findById(createPostReplyDto.getUserId()).orElseThrow(() -> new BasicException("없는 회원입니다.")))
        .content(createPostReplyDto.getContent())
        .post(post)
        .likeCount(0L)
        .dislikeCount(0L)
        .build();

        PostReply result = postReplyRepository.save(postReply);

        return result.getId();
    }

    // 게시글 댓글 조회
    public List<ReplyDto> readPostReply(Long postId, Long viewerId, Pageable pageable) {

        List<PostReply> replys = postReplyRepository.findByPostIdAndDeleted(postId, pageable, false);

        List<ReplyDto> result = new ArrayList<ReplyDto>();

        for (PostReply reply : replys) {
            Boolean isLike = false;
            Boolean isDislike = false;
            if (viewerId != null) {
                isLike = likePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(reply.getId(), viewerId, false).isPresent();
                isDislike = dislikePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(reply.getId(), viewerId, false).isPresent();
            }

            SimpleWriterDto writer = !reply.getUser().getDeleted() ? new SimpleWriterDto(reply.getUser()) : new SimpleDeletedWriterDto();

            ReplyDto commentDto = ReplyDto.builder()
            .id(reply.getId())
            .writer(writer)
            .createdDate(reply.getCreatedDate())
            .isLike(isLike)
            .likeCount(reply.getLikeCount())
            .isDislike(isDislike)
            .dislikeCount(reply.getDislikeCount())
            .content(reply.getContent())
            .build();

            result.add(commentDto);
        }
        return result;
    }

    // 게시글 리스트 조회
    public List<ReadPostsDto> readPosts(Pageable pageable, PostCondDto cond, Boolean recent) {
        List<ReadSimplePostDto> posts = postRepository.findListPosts(pageable, cond, recent);
        List<ReadPostsDto> result = new ArrayList<>();

        for (ReadSimplePostDto simplePost : posts) {
            
            List<String> words = new ArrayList<String>();
            List<Hashtag> hashtags = hashtagRepository.findPostHashtags(simplePost.getPostId());
            for (Hashtag hashtag : hashtags) {
                words.add(hashtag.getWord());
            }
            
            List<String> fileNames = new ArrayList<String>();
            List<Media> media = mediaRepository.findPostMedia(simplePost.getPostId());
            for (Media medium : media) {
                fileNames.add(medium.getFileName());
            }

            User user = userRepository.findById(simplePost.getWriterId()).get();
            SimpleWriterDto writer = !user.getDeleted() ? new SimpleWriterDto(user) : new SimpleDeletedWriterDto();
            
            result.add(new ReadPostsDto(simplePost, writer, words, fileNames));
        }

        return result;
    }
    
    // 게시글 좋아요, 좋아요 취소
    // 좋아요 클릭
    public LikeDislikeResDto likePost(TargetIdUserIdDto req) {
        Long postId = req.getTargetId();
        Long userId = req.getUserId();
        
        Post post = postRepository.findById(postId).orElseThrow(() -> new BasicException("게시글을 찾을 수 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));;
        
        // 좋아요가 눌려있으면 like -1
        if (likePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, false).isPresent()) {
            likePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, false).get().delete();

            post.setLikeCount(post.getLikeCount() - 1);
        }
        // like +1
        else {
            // 싫어요가 눌려있으면 dislike -1
            if (dislikePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, false).isPresent()) {
                dislikePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, false).get().delete();
                
                post.setDislikeCount(post.getDislikeCount() - 1);
            }

            // like 추가
            if (likePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, true).isPresent()) {
                likePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, true).get().revive();

            }
            else {
                LikePost likePost = LikePost.builder().post(post).user(user).build();
                likePostRepository.save(likePost);
            }

            post.setLikeCount(post.getLikeCount() + 1);
        }

        LikeDislikeResDto result = LikeDislikeResDto.builder()
            .id(postId)
            .isLike(likePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, false).isPresent())
            .isDislike(dislikePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, false).isPresent())
            .likeCount(post.getLikeCount())
            .dislikeCount(post.getDislikeCount())
            .build();

        return result;
    }

    // 게시글 싫어요, 싫어요 취소
    // 싫어요 클릭
    public LikeDislikeResDto dislikePost(TargetIdUserIdDto req) {
        Long postId = req.getTargetId();
        Long userId = req.getUserId();

        Post post = postRepository.findById(postId).orElseThrow(() -> new BasicException("청원을 찾을 수 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));;
        
         // 싫어요가 눌려있으면 dislike -1
         if (dislikePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, false).isPresent()) {
            dislikePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, false).get().delete();

            post.setDislikeCount(post.getDislikeCount() - 1);
        }
        // like +1
        else {
            // 좋아요가 눌려있으면 like -1
            if (likePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, false).isPresent()) {
                likePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, false).get().delete();
                
                post.setLikeCount(post.getLikeCount() - 1);
            }

            // dislike 추가
            if (dislikePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, true).isPresent()) {
                dislikePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, true).get().revive();

            }
            else {
                DislikePost dislikePost = DislikePost.builder().post(post).user(user).build();
                dislikePostRepository.save(dislikePost);
            }

            post.setDislikeCount(post.getDislikeCount() + 1);
        }

        LikeDislikeResDto result = LikeDislikeResDto.builder()
            .id(postId)
            .isLike(likePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, false).isPresent())
            .isDislike(dislikePostRepository.findByPostIdAndUserIdAndDeleted(postId, userId, false).isPresent())
            .likeCount(post.getLikeCount())
            .dislikeCount(post.getDislikeCount())
            .build();

        return result;
    }

    // 댓글 좋아요 좋아요 취소
    // 댓글 좋아요 클릭
    public LikeDislikeResDto likePostReply(TargetIdUserIdDto req) {
        Long replyId = req.getTargetId();
        Long userId = req.getUserId();

        PostReply reply = postReplyRepository.findById(replyId).orElseThrow(() -> new BasicException("댓글을 찾을 수 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        
        // 좋아요가 눌려있으면 like -1
        if (likePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent()) {
            likePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, false).get().delete();

            reply.setLikeCount(reply.getLikeCount() - 1);
        }
        // like +1
        else {
            // 싫어요가 눌려있으면 dislike -1
            if (dislikePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent()) {
                dislikePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, false).get().delete();
                
                reply.setDislikeCount(reply.getDislikeCount() - 1);
            }

            // like 추가
            if (likePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, true).isPresent()) {
                likePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, true).get().revive();

            }
            else {
                LikePostReply likePostReply = LikePostReply.builder().postReply(reply).user(user).build();
                likePostReplyRepository.save(likePostReply);
            }

            reply.setLikeCount(reply.getLikeCount() + 1);
        }

        LikeDislikeResDto result = LikeDislikeResDto.builder()
            .id(replyId)
            .isLike(likePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent())
            .isDislike(dislikePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent())
            .likeCount(reply.getLikeCount())
            .dislikeCount(reply.getDislikeCount())
            .build();

        return result;
    }
    
    // 댓글 싫어요 싫어요 취소
    // 댓글 싫어요 클릭
    public LikeDislikeResDto dislikePostReply(TargetIdUserIdDto req) {
        Long replyId = req.getTargetId();
        Long userId = req.getUserId();

        PostReply reply = postReplyRepository.findById(replyId).orElseThrow(() -> new BasicException("댓글을 찾을 수 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new BasicException("유저를 찾을 수 없습니다."));
        
        // 싫어요가 눌려있으면 dislike -1
        if (dislikePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent()) {
            dislikePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, false).get().delete();

            reply.setDislikeCount(reply.getDislikeCount() - 1);
        }
        // like +1
        else {
            // 좋아요가 눌려있으면 like -1
            if (likePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent()) {
                likePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, false).get().delete();
                
                reply.setLikeCount(reply.getLikeCount() - 1);
            }

            // dislike 추가
            if (dislikePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, true).isPresent()) {
                dislikePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, true).get().revive();

            }
            else {
                DislikePostReply dislikePostReply = DislikePostReply.builder().postReply(reply).user(user).build();
                dislikePostReplyRepository.save(dislikePostReply);
            }

            reply.setDislikeCount(reply.getDislikeCount() + 1);
        }

        LikeDislikeResDto result = LikeDislikeResDto.builder()
            .id(replyId)
            .isLike(likePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent())
            .isDislike(dislikePostReplyRepository.findByPostReplyIdAndUserIdAndDeleted(replyId, userId, false).isPresent())
            .likeCount(reply.getLikeCount())
            .dislikeCount(reply.getDislikeCount())
            .build();

        return result;
    }

    public List<ReadPostsDto> getPostsByHashtag(Pageable pageable, String hash) {
        List<ReadSimplePostDto> posts = hashtagRepository.getPostByHashtag(pageable, hash);
        List<ReadPostsDto> result = new ArrayList<>();

        for (ReadSimplePostDto simplePost : posts) {
            
            List<String> words = new ArrayList<String>();
            List<Hashtag> hashtags = hashtagRepository.findPostHashtags(simplePost.getPostId());
            for (Hashtag hashtag : hashtags) {
                words.add(hashtag.getWord());
            }
            
            List<String> fileNames = new ArrayList<String>();
            List<Media> media = mediaRepository.findPostMedia(simplePost.getPostId());
            for (Media medium : media) {
                fileNames.add(medium.getFileName());
            }

            User user = userRepository.findById(simplePost.getWriterId()).get();
            SimpleWriterDto writer = !user.getDeleted() ? new SimpleWriterDto(user) : new SimpleDeletedWriterDto();
            
            result.add(new ReadPostsDto(simplePost, writer, words, fileNames));
        }

        return result;
    }
}
