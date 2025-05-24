package com.example.photomory.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ALBUM_MEMBERS")
@IdClass(AlbumMembers.AlbumMembersId.class)
public class AlbumMembers {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private MyAlbum myAlbum;



    // 기본 생성자
    public AlbumMembers() {}

    // Getter / Setter
    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public MyAlbum getMyAlbum() {
        return myAlbum;
    }

    public void setMyAlbum(MyAlbum myAlbum) {
        this.myAlbum = myAlbum;
    }

    // 복합키 클래스
    public static class AlbumMembersId implements Serializable {
        private Long userEntity;   // UserEntity PK 타입과 일치
        private Integer myAlbum;   // MyAlbum PK 타입과 일치

        public AlbumMembersId() {}

        public AlbumMembersId(Long userEntity, Integer myAlbum) {
            this.userEntity = userEntity;
            this.myAlbum = myAlbum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AlbumMembersId)) return false;
            AlbumMembersId that = (AlbumMembersId) o;
            return Objects.equals(userEntity, that.userEntity) &&
                    Objects.equals(myAlbum, that.myAlbum);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userEntity, myAlbum);
        }
    }
}
