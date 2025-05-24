package com.example.photomory.dto;

import java.time.LocalDateTime;
import lombok.Getter; // Lombok 사용 시
import lombok.Setter; // Lombok 사용 시

@Getter // Lombok 사용 시
@Setter // Lombok 사용 시
public class AlbumCreateRequestDto {

    private String albumName;
    private String albumTag;
    private LocalDateTime albumMakingTime;
    private String albumDescription;
    private Long postId; // <-- post_id 값을 직접 받기 위한 필드 추가

    // Lombok @NoArgsConstructor가 없다면 기본 생성자 필요
    public AlbumCreateRequestDto() {}

     public String getAlbumName() { return albumName; }
     public void setAlbumName(String albumName) { this.albumName = albumName; }
     public String getAlbumTag() { return albumTag; }
     public void setAlbumTag(String albumTag) { this.albumTag = albumTag; }
     public LocalDateTime getAlbumMakingTime() { return albumMakingTime; }
     public void setAlbumMakingTime(LocalDateTime albumMakingTime) { this.albumMakingTime = albumMakingTime; }
     public String getAlbumDescription() { return albumDescription; }
     public void setAlbumDescription(String albumDescription) { this.albumDescription = albumDescription; }
     public Long getPostId() { return postId; }
     public void setPostId(Long postId) { this.postId = postId; }
}