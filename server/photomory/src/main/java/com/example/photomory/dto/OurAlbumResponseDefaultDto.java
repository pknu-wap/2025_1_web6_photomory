package com.example.photomory.dto; // 실제 패키지 경로에 맞게 수정해주세요.

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OurAlbumResponseDefaultDto {
    private Long groupId;
    private String groupName;
    private List<Member> members;
    private List<Album> albums;

    // --- 중첩 클래스 정의 시작 ---

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Member { // static 키워드 중요!
        private Long userId;
        private String userName;
        private String userPhotourl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Album { // static 키워드 중요!
        private Long albumId;
        private String albumName;
        private String albumDescription;
        private String albumTag;
        private String albumMakingtime; // 날짜 형식이면 LocalDate 또는 String
        private List<Photo> photos;
        private List<Comment> comments;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Photo { // static 키워드 중요!
        private Long photoId;
        private String photoUrl;
        private String photoName;
        private Long postId;
        private String photoMakingtime; // 날짜 형식이면 LocalDate 또는 String
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Comment { // static 키워드 중요!
        private Long albumId;
        private Long photoId;
        private Long userId;
        private String commentText;
    }

    // --- 중첩 클래스 정의 끝 ---
}