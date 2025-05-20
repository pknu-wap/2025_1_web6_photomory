package com.example.photomory.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ALBUM_MEMBERS")
@IdClass(AlbumMembers.AlbumMembersId.class)
public class AlbumMembers {

    @Id
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Id
    @Column(name = "group_id", insertable = false, updatable = false)
    private Integer groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private MyAlbum myAlbum;

    // 기본 생성자
    public AlbumMembers() {}

    // getter/setter
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

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
        private Integer userId;
        private Integer groupId;

        public AlbumMembersId() {}

        public AlbumMembersId(Integer userId, Integer groupId) {
            this.userId = userId;
            this.groupId = groupId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AlbumMembersId)) return false;
            AlbumMembersId that = (AlbumMembersId) o;
            return Objects.equals(userId, that.userId) &&
                    Objects.equals(groupId, that.groupId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, groupId);
        }
    }
}
