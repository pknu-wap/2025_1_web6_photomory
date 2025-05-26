package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.*; // Lombok 어노테이션 임포트

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "POST") // 테이블 이름은 대문자로 유지
@Getter
@Setter
@NoArgsConstructor // Lombok - 기본 생성자
@AllArgsConstructor // Lombok - 모든 필드를 포함하는 생성자
@Builder // Lombok - 빌더 패턴
public class Post {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId; // 타입 Integer로 변경

    @Column(name = "post_text") // length는 기본값 255. 필요시 명시
    private String postText;

    @Column(name = "post_description") // nullable=true가 명시되지 않으면 기본값으로 nullable=true
    private String postDescription;

    @Column(name = "photo_url") // nullable=true가 명시되지 않으면 기본값으로 nullable=true
    private String photoUrl;

    @Column(name = "location") // nullable=true가 명시되지 않으면 기본값으로 nullable=true
    private String location;

    @Column(name = "making_time") // 컬럼 이름 다시 making_time으로 변경
    private LocalDateTime makingTime;

    @Column(name = "likes_count")
    private Integer likesCount; // 기본값은 코드에서 설정하거나 DB에서 설정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false) // 앨범 ID는 필수
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // user_id는 NULL 허용하지 않음
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "post_tag",  // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public void setAlbum(Album album) {
        this.album = album;
        if (album != null && !album.getPosts().contains(this)) {
            album.getPosts().add(this);
        }
    }

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    @Column(name = "comment_count", nullable = false)
    private int commentCount;



}