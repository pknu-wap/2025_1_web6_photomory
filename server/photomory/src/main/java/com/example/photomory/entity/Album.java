package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList; // List를 사용하기 위해 추가
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ALBUM") // 테이블명 확인: 대문자 'ALBUM'으로 되어있습니다.
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

    // ----- 다음 줄을 삭제하거나 주석 처리합니다. (가장 중요!) -----
    // @Column(name = "post_id", nullable = false)
    // private Long postId;
    // -------------------------------------------------------------

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>(); // 이 앨범에 속한 여러 게시물들을 나타냅니다. (초기화)

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // (초기화)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "myalbum_id")
    // DB의 myalbum_id 컬럼이 NULL을 허용하는 경우 nullable = false를 명시하지 않습니다.
    // 만약 항상 MyAlbum이 있어야 한다면 nullable = false를 붙일 수 있습니다. (현재 DB 스키마에 따름)
    private MyAlbum myAlbum;

    @ManyToMany
    @JoinTable(
            name = "album_tag", // 조인 테이블 이름 (확인 필요)
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // 편의 메서드 (필요하다면 추가)
    public void addPost(Post post) {
        this.posts.add(post);
        if (post.getAlbum() != this) {
            post.setAlbum(this);
        }
    }
}