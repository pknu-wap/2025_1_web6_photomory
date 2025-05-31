package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EveryAlbum") // 데이터베이스 테이블 이름과 일치
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EveryAlbum {

    @OneToMany(mappedBy = "everyAlbum", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Comment> comments = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Integer albumId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 앨범을 만든 사용자
    private UserEntity user;

    @Column(name = "album_name", nullable = false, length = 45)
    private String albumName;

    @Column(name = "album_tag", length = 45) // Nullable = YES
    private String albumTag;

    @Column(name = "album_makingtime", nullable = false)
    private LocalDateTime albumMakingTime;

    @Column(name = "album_description", nullable = false, length = 250)
    private String albumDescription;

    @OneToMany(mappedBy = "everyAlbum", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<EveryPost> everyPosts = new HashSet<>();

}