// AlbumConnect.java 파일의 내용 (이렇게 변경해야 합니다)
package com.example.photomory.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ALBUM_CONNECT") // 이 테이블명은 유지
@IdClass(AlbumConnect.AlbumConnectId.class)
public class AlbumConnect {

    @Id
    @Column(name = "our_album_id") // OurAlbum의 ID를 참조
    private Integer ourAlbumId;

    // 만약 EveryAlbum과의 연결도 필요하다면 아래를 추가
    // @Id
    // @Column(name = "every_album_id")
    // private Integer everyAlbumId;

    @Id
    @Column(name = "user_id") // UserEntity의 ID를 참조
    private Long userId;

    // Getters and setters
    public Integer getOurAlbumId() {
        return ourAlbumId;
    }

    public void setOurAlbumId(Integer ourAlbumId) {
        this.ourAlbumId = ourAlbumId;
    }

    // public Integer getEveryAlbumId() { ... } (everyAlbumId 추가 시)
    // public void setEveryAlbumId(Integer everyAlbumId) { ... } (everyAlbumId 추가 시)

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Composite key class
    public static class AlbumConnectId implements Serializable {
        private Integer ourAlbumId;
        // private Integer everyAlbumId; // everyAlbumId 추가 시
        private Long userId;

        public AlbumConnectId() {}

        public AlbumConnectId(Integer ourAlbumId, Long userId) { // everyAlbumId 추가 시 생성자 변경
            this.ourAlbumId = ourAlbumId;
            this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AlbumConnectId)) return false;
            AlbumConnectId that = (AlbumConnectId) o;
            return Objects.equals(ourAlbumId, that.ourAlbumId) &&
                    Objects.equals(userId, that.userId);
            // everyAlbumId 추가 시 변경:
            // return Objects.equals(ourAlbumId, that.ourAlbumId) &&
            //        Objects.equals(everyAlbumId, that.everyAlbumId) &&
            //        Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ourAlbumId, userId);
            // everyAlbumId 추가 시 변경:
            // return Objects.hash(ourAlbumId, everyAlbumId, userId);
        }
    }
}