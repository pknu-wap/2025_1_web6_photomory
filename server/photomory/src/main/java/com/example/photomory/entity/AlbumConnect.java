package com.example.photomory.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ALBUM_CONNECT")
@IdClass(AlbumConnect.AlbumConnectId.class)
public class AlbumConnect {

    @Id
    @Column(name = "album_id2")
    private Integer albumId2;

    @Id
    @Column(name = "user_id2")
    private Integer userId2;

    // Getters and setters
    public Integer getAlbumId2() {
        return albumId2;
    }

    public void setAlbumId2(Integer albumId2) {
        this.albumId2 = albumId2;
    }

    public Integer getUserId2() {
        return userId2;
    }

    public void setUserId2(Integer userId2) {
        this.userId2 = userId2;
    }

    // Composite key class
    public static class AlbumConnectId implements Serializable {
        private Integer albumId2;
        private Integer userId2;

        public AlbumConnectId() {}

        public AlbumConnectId(Integer albumId2, Integer userId2) {
            this.albumId2 = albumId2;
            this.userId2 = userId2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AlbumConnectId)) return false;
            AlbumConnectId that = (AlbumConnectId) o;
            return Objects.equals(albumId2, that.albumId2) &&
                    Objects.equals(userId2, that.userId2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(albumId2, userId2);
        }
    }
}
