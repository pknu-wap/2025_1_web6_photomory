package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

import com.example.photomory.entity.UserGroup;  // 꼭 추가!

@Entity
@Table(name = "OUR_ALBUM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OurAlbum {

    @OneToMany(mappedBy = "ourAlbum", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Comment> comments = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Integer albumId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "album_name", nullable = false, length = 45)
    private String albumName;

    @Column(name = "album_tag", length = 45)
    private String albumTag;

    @Column(name = "album_makingtime", nullable = false)
    private LocalDateTime albumMakingTime;

    @Column(name = "album_description", nullable = false, length = 250)
    private String albumDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")  // 외래 키 컬럼
    private UserGroup userGroup;  // 필드명 통일

    // OurPost와 1:N 관계 설정 (album이 여러 post를 가짐)
    @OneToMany(mappedBy = "ourAlbum", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<OurPost> posts = new HashSet<>();

    @Column(name = "is_group", nullable = false)
    private Boolean isGroup = false;

}
