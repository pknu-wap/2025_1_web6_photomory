package com.example.photomory.dto;

import com.example.photomory.entity.Album;
import com.example.photomory.entity.Post;
import java.time.format.DateTimeFormatter;

public class OurAlbumResponseDto {

    private Integer albumId;
    private String albumName;
    private String albumDescription;
    private String albumTag;
    private String albumMakingTime;

    private String userName;
    private String userPhotoUrl;

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

        // posts 리스트에서 대표 post 첫 번째 꺼를 사용
        if (album.getPosts() != null && !album.getPosts().isEmpty()) {
            Post post = album.getPosts().get(0);
            if (post.getUser() != null) {
                dto.setUserName(post.getUser().getUserName());
                dto.setUserPhotoUrl(post.getUser().getUserPhotourl());
            }
        }

        return dto;
    }

    // getters/setters

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
}
