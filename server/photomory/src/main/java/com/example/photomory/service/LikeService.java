package com.example.photomory.service;

import com.example.photomory.entity.*;
import com.example.photomory.repository.*;
import com.example.photomory.domain.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    private final OurPostRepository ourPostRepository;
    private final MyPostRepository myPostRepository;
    private final EveryPostRepository everyPostRepository;

    private final NotificationService notificationService;

    /**
     * postType: "OUR", "MY", "EVERY" 중 하나를 문자열로 받음
     */
    public void toggleLike(Long postId, String userEmail, String postType) {
        UserEntity user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));

        switch (postType.toUpperCase()) {
            case "OUR":
                OurPost ourPost = ourPostRepository.findById(postId.intValue())
                        .orElseThrow(() -> new RuntimeException("OUR 게시글을 찾을 수 없습니다"));

                likeRepository.findByUserAndOurPost(user, ourPost).ifPresentOrElse(
                        likeRepository::delete,
                        () -> {
                            Like like = Like.builder()
                                    .user(user)
                                    .ourPost(ourPost)
                                    .commentId(0)
                                    .likesCount(1)
                                    .postType("OUR")
                                    .build();
                            likeRepository.save(like);
                        }
                );

                sendNotification(user, ourPost.getUser(), postId, "OUR", ourPost);
                break;

            case "MY":
                MyPost myPost = myPostRepository.findById(postId.intValue())
                        .orElseThrow(() -> new RuntimeException("MY 게시글을 찾을 수 없습니다"));

                likeRepository.findByUserAndMyPost(user, myPost).ifPresentOrElse(
                        likeRepository::delete,
                        () -> {
                            Like like = Like.builder()
                                    .user(user)
                                    .myPost(myPost)
                                    .commentId(0)
                                    .likesCount(1)
                                    .postType("MY")
                                    .build();
                            likeRepository.save(like);
                        }
                );

                sendNotification(user, myPost.getUser(), postId, "MY", myPost);
                break;

            case "EVERY":
                EveryPost everyPost = everyPostRepository.findById(postId.intValue())
                        .orElseThrow(() -> new RuntimeException("EVERY 게시글을 찾을 수 없습니다"));

                likeRepository.findByUserAndEveryPost(user, everyPost).ifPresentOrElse(
                        likeRepository::delete,
                        () -> {
                            Like like = Like.builder()
                                    .user(user)
                                    .everyPost(everyPost)
                                    .postId(postId)
                                    .commentId(0)
                                    .likesCount(1)
                                    .postType("EVERY")
                                    .build();
                            likeRepository.save(like);
                        }
                );

                sendNotification(user, everyPost.getUser(), postId, "EVERY", everyPost);
                break;

            default:
                throw new IllegalArgumentException("알 수 없는 포스트 타입입니다.");
        }
    }

    public Long getLikeCount(Long postId, String postType) {
        switch (postType.toUpperCase()) {
            case "OUR":
                OurPost ourPost = ourPostRepository.findById(postId.intValue())
                        .orElseThrow(() -> new RuntimeException("OUR 게시글을 찾을 수 없습니다"));
                return likeRepository.countByOurPost(ourPost);

            case "MY":
                MyPost myPost = myPostRepository.findById(postId.intValue())
                        .orElseThrow(() -> new RuntimeException("MY 게시글을 찾을 수 없습니다"));
                return likeRepository.countByMyPost(myPost);

            case "EVERY":
                EveryPost everyPost = everyPostRepository.findById(postId.intValue())
                        .orElseThrow(() -> new RuntimeException("EVERY 게시글을 찾을 수 없습니다"));
                return likeRepository.countByEveryPost(everyPost);

            default:
                throw new IllegalArgumentException("알 수 없는 포스트 타입입니다.");
        }
    }

    // 좋아요 알림 전송 공통 메서드
    private void sendNotification(UserEntity liker, UserEntity postWriter, Long postId, String postType, Object postEntity) {
        if (liker.getUserId().equals(postWriter.getUserId())) {
            // 작성자 본인일 경우 알림 전송하지 않음
            return;
        }

        // 게시글 제목 가져오기 (예: 첫번째 사진의 타이틀, 없으면 "제목 없음")
        String postTitle = "제목 없음";

        if (postEntity instanceof OurPost) {
            OurPost p = (OurPost) postEntity;
            postTitle = p.getPhotos().stream()
                    .map(Photo::getTitle)
                    .findFirst()
                    .orElse(postTitle);
        } else if (postEntity instanceof MyPost) {
            MyPost p = (MyPost) postEntity;
            postTitle = p.getPhotos().stream()
                    .map(Photo::getTitle)
                    .findFirst()
                    .orElse(postTitle);
        } else if (postEntity instanceof EveryPost) {
            EveryPost p = (EveryPost) postEntity;
            postTitle = p.getPhotos().stream()
                    .map(Photo::getTitle)
                    .findFirst()
                    .orElse(postTitle);
        }

        String message = liker.getUserName() + "님이 " + postTitle + " 게시글에 좋아요를 눌렀습니다.";

        notificationService.sendNotification(
                postWriter.getUserId(),  // 수신자
                liker.getUserId(),       // 발신자
                message,
                NotificationType.LIKE,
                postId
        );
    }
}
