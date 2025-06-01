package com.example.photomory.dto;

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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Member {
        private Long userId;
        private String userName;
        private String userPhotourl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Album {
        private Integer albumId;
        private String albumName;
        private String albumDescription;
        private String albumTag;
        private String albumMakingtime;
        // photos, comments 삭제
        private List<Post> posts;  // Post 리스트 추가
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Photo {
        private Integer photoId;
        private String photoUrl;
        private String photoName;
        private Integer postId;
        private String photoMakingtime;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Comment {
        private Long userId;
        private String commentText;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Post {
        private Integer postId;
        private String postContent;
        private String postImageUrl;
        private String postMakingTime;
        private List<Comment> comments;
    }
}
