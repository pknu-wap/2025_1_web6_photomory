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
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity userEntity;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "myalbum_id")
    private MyAlbum myAlbum;

    // 기본 생성자
    public AlbumMembers() {}

    // getter/setter
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

    // Composite key class
    public static class AlbumMembersId implements Serializable {
        private Integer userEntity;
        private Integer myAlbum;

        public AlbumMembersId() {}

        public AlbumMembersId(Integer userEntity, Integer myAlbum) {
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
