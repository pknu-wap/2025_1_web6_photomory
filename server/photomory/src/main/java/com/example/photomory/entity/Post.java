package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import com.example.photomory.entity.Album;
// import java.util.ArrayList; // List로 변경 시 필요 (현재는 Set 유지)
// import java.util.List;      // List로 변경 시 필요 (현재는 Set 유지)

// *** 이 라인을 추가했습니다: ***
import com.example.photomory.entity.Album; // Album 엔티티의 실제 패키지 경로로 확인해주세요.

@Entity
@Table(name = "POST")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)

    private Set<Photo> photos = new HashSet<>(); // Set 타입 유지

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "post_description")
    private String postDescription;


    @Column(name = "location")
    private String location;

    @Column(name = "making_time")
    private LocalDateTime makingTime;

    @Column(name = "likes_count")
    private Integer likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album; // Album 클래스 임포트 확인

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // setAlbum 메서드: Album 클래스가 올바르게 임포트되어 있어야 합니다.
    public void setAlbum(Album album) {
        this.album = album;
        // album.getPosts() 호출을 위해 Album 엔티티에 'posts' 필드와 'getPosts()' 메서드가 있어야 합니다.
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


    @Column(name = "photo_url") // nullable=true가 명시되지 않으면 기본값으로 nullable=true
    private String photoUrl;

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
        if (photo.getPost() != this) { // 무한 루프 방지
            photo.setPost(this);
        }
    }
}