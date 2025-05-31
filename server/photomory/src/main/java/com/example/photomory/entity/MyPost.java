package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime; // LocalDateTime은 사용되지 않지만, 다른 엔티티에서 필요할 수 있으므로 유지
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MyPost") // 데이터베이스 테이블 이름 MyPost에 정확히 매핑
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id") // DB 컬럼명과 일치
    private Integer postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // DB 컬럼명과 일치
    private UserEntity user;

    @Column(name = "post_text", nullable = false, length = 250) // DB 컬럼명과 일치
    private String postText; // `title` 대신 `postText`로 변경

    @Column(name = "post_description", nullable = false, length = 250) // DB 컬럼명과 일치
    private String postDescription; // `content` 대신 `postDescription`으로 변경

    @Column(name = "location", nullable = false, length = 250) // DB 컬럼명에 따라 새로 추가
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false) // DB 컬럼명과 일치
    private MyAlbum myAlbum; // <--- MyAlbum 엔티티를 참조하는 필드 추가


    @OneToMany(mappedBy = "myPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Photo> photos = new HashSet<>();

}