package com.example.photomory.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ALBUM_MEMBERS")
@IdClass(AlbumMembers.AlbumMembersId.class)
public class AlbumMembers {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "group_id")
    private Integer groupId;

    // Getters and setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
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
