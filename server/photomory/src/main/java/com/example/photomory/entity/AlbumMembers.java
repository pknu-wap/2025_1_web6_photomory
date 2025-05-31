package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ALBUM_MEMBERS")
@IdClass(AlbumMembers.AlbumMembersId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumMembers {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "our_album_id", nullable = false) // 'album_id'에서 'our_album_id'로 컬럼명 변경
    private OurAlbum ourAlbum;

    public static class AlbumMembersId implements Serializable {
        private Long userEntity;   // UserEntity PK 타입과 일치해야 합니다. (user_id)
        private Integer ourAlbum;  // OurAlbum PK 타입과 일치해야 합니다. (our_album_id)

        public AlbumMembersId() {}

        public AlbumMembersId(Long userEntity, Integer ourAlbum) {
            this.userEntity = userEntity;
            this.ourAlbum = ourAlbum;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AlbumMembersId)) return false;
            AlbumMembersId that = (AlbumMembersId) o;
            return Objects.equals(userEntity, that.userEntity) &&
                    Objects.equals(ourAlbum, that.ourAlbum);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userEntity, ourAlbum);
        }
    }
}