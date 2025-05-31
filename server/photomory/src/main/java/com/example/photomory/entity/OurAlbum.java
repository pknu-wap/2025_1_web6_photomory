package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList; // List를 위한 임포트
import java.util.List;

@Entity
@Table(name = "OUR_ALBUM") // 테이블명 유지
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OurAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Integer albumId;

    // OurAlbum을 생성한 사용자 (앨범의 소유주 또는 관리자).
    // 이 필드는 앨범의 'creator'를 나타내기에 적합합니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "album_name", nullable = false, length = 45)
    private String albumName;

    @Column(name = "album_tag", length = 45)
    private String albumTag;

    @Column(name = "album_makingtime", nullable = false)
    private LocalDateTime albumMakingTime; // 필드명은 이미 올바릅니다.

    @Column(name = "album_description", nullable = false, length = 250)
    private String albumDescription;

    @OneToMany(mappedBy = "ourAlbum", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default // Lombok @Builder를 사용할 때 초기화를 위해 필요
    private List<OurPost> posts = new ArrayList<>(); // List<Post> -> List<OurPost>로 타입 변경

}