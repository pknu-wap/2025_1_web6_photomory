package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor; // Lombok AllArgsConstructor 추가
import lombok.Builder;             // Lombok Builder 추가
import lombok.Getter;              // Lombok Getter 추가
import lombok.NoArgsConstructor;   // Lombok NoArgsConstructor 추가
import lombok.Setter;              // Lombok Setter 추가

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ALBUM_MEMBERS")
@IdClass(AlbumMembers.AlbumMembersId.class)
@Getter // 모든 필드에 대한 Getter 자동 생성
@Setter // 모든 필드에 대한 Setter 자동 생성
@NoArgsConstructor // 인자 없는 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 자동 생성
@Builder // 빌더 패턴 사용 가능
public class AlbumMembers {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private OurAlbum ourAlbum; // 필드 이름이 'ourAlbum'이므로 setOurAlbum() 메서드가 사용됩니다.

    public static class AlbumMembersId implements Serializable {
        private Long userEntity;   // UserEntity PK 타입과 일치 (user_id)
        private Integer ourAlbum;  // OurAlbum PK 타입과 일치 (album_id)

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