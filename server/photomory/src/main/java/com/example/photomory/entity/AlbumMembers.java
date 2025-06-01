package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "GROUP_MEMBERSHIP") // 테이블 이름을 명확하게 'GROUP_MEMBERSHIP'으로 변경하는 것을 강력히 권장
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumMembers { // 엔티티 이름도 'GroupMembership'으로 변경 고려

    @EmbeddedId
    private AlbumMembersId id; // EmbeddedId 이름도 'GroupMembershipId'로 변경 고려

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userGroupId")
    @JoinColumn(name = "user_group_id", nullable = false)
    private UserGroup userGroup; // 그룹 멤버십을 나타내기 위한 필드

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class AlbumMembersId implements Serializable { // 클래스 이름도 'GroupMembershipId'로 변경 고려

        @Column(name = "user_id")
        private Long userId;

        @Column(name = "user_group_id")
        private Long userGroupId;
    }
}