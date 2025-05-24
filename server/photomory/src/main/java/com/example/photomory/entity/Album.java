package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ALBUM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Integer albumId;

    @Column(name = "album_name", nullable = false)
    private String albumName;

    @Column(name = "album_tag")
    private String albumTag;

    @Column(name = "album_makingtime", nullable = false)
    private LocalDateTime albumMakingTime;

    @Column(name = "album_description", nullable = false)
    private String albumDescription;

    // 이 post_id는 ALBUM 테이블에서 필수(NOT NULL) 컬럼입니다.
    // POST 테이블의 기본 키를 참조하는 외래 키입니다.
    @ManyToOne(fetch = FetchType.LAZY) // Post 엔티티는 필요할 때만 로드하도록 지연 로딩 설정
    @JoinColumn(name = "post_id", nullable = false) // ALBUM 테이블의 'post_id' 컬럼과 매핑되며, DB의 'NO' (NOT NULL) 제약 조건에 맞춰 nullable = false로 명시합니다.
    private Post representativePost; // 이 앨범을 대표하는 게시물 등을 의미할 수 있습니다.

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts; // 이 앨범에 속한 여러 게시물들을 나타냅니다.

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "myalbum_id") // DB의 myalbum_id 컬럼은 NULL을 허용하므로, 여기서는 nullable = false를 명시하지 않습니다.
    private MyAlbum myAlbum;

    @ManyToMany
    @JoinTable(
            name = "album_tag",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
}