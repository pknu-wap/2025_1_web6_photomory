package com.example.photomory.dto;

import com.example.photomory.entity.Album;
import com.example.photomory.entity.Post;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class OurAlbumResponseDto {

    private Integer albumId;
    private String albumName;
    private String albumDescription;
    private String albumTag;
    private String albumMakingTime;

    private String userName;
    private String userPhotoUrl;

    // 추가: 앨범 내 게시물 리스트 (간략화된 DTO)
    private List<PostSummaryDto> posts;

    public OurAlbumResponseDto() {}

    public static OurAlbumResponseDto fromEntity(Album album) {
        OurAlbumResponseDto dto = new OurAlbumResponseDto();
        dto.setAlbumId(album.getAlbumId());
        dto.setAlbumName(album.getAlbumName());
        dto.setAlbumDescription(album.getAlbumDescription());
        dto.setAlbumTag(album.getAlbumTag());

        if (album.getAlbumMakingTime() != null) {
            dto.setAlbumMakingTime(album.getAlbumMakingTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        // 대표 사용자 정보: 게시물이 있다면 첫 게시물 작성자 기준으로 세팅
        if (album.getPosts() != null && !album.getPosts().isEmpty()) {
            Post firstPost = album.getPosts().get(0);
            if (firstPost.getUser() != null) {
                dto.setUserName(firstPost.getUser().getUserName());
                dto.setUserPhotoUrl(firstPost.getUser().getUserPhotourl());
            }
            // 앨범 내 게시물 리스트를 간략화 DTO로 변환
            dto.setPosts(album.getPosts().stream()
                    .map(PostSummaryDto::fromEntity)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    // getters / setters
    public Integer getAlbumId() { return albumId; }
    public void setAlbumId(Integer albumId) { this.albumId = albumId; }
    public String getAlbumName() { return albumName; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }
    public String getAlbumDescription() { return albumDescription; }
    public void setAlbumDescription(String albumDescription) { this.albumDescription = albumDescription; }
    public String getAlbumTag() { return albumTag; }
    public void setAlbumTag(String albumTag) { this.albumTag = albumTag; }
    public String getAlbumMakingTime() { return albumMakingTime; }
    public void setAlbumMakingTime(String albumMakingTime) { this.albumMakingTime = albumMakingTime; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserPhotoUrl() { return userPhotoUrl; }
    public void setUserPhotoUrl(String userPhotoUrl) { this.userPhotoUrl = userPhotoUrl; }
    public List<PostSummaryDto> getPosts() { return posts; }
    public void setPosts(List<PostSummaryDto> posts) { this.posts = posts; }


    // 내부 static 클래스: 게시물 간략 DTO
    public static class PostSummaryDto {
        private Integer postId;  // Integer로 맞춤
        private String postText;
        private String photoUrl;

        public PostSummaryDto() {}

        public static PostSummaryDto fromEntity(Post post) {
            PostSummaryDto dto = new PostSummaryDto();
            dto.setPostId(post.getPostId());
            dto.setPostText(post.getPostText());

            if (post.getPhotos() != null && !post.getPhotos().isEmpty()) {
                dto.setPhotoUrl(post.getPhotos().get(0).getPhotoUrl());
            } else {
                dto.setPhotoUrl(null);
            }
            return dto;
        }

        public Integer getPostId() { return postId; }
        public void setPostId(Integer postId) { this.postId = postId; }
        public String getPostText() { return postText; }
        public void setPostText(String postText) { this.postText = postText; }
        public String getPhotoUrl() { return photoUrl; }
        public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    }
}
