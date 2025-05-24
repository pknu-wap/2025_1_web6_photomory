package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @Column(name = "post_text", length = 500)
    private String postText;

    @Column(name = "post_description", length = 500)
    private String postDescription;

    @Column(name = "photo_url", length = 255)
    private String photoUrl; // Post 엔티티에 직접 photoUrl 필드 존재

    @Column(name = "likes_count", nullable = false)
    private Integer likesCount = 0;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "post_making_time")
    private LocalDateTime makingTime;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // Post와 Tag의 One-to-Many 관계
    // Tag 엔티티의 'post' 필드(ManyToOne)에 의해 매핑됩니다.
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tag> tags = new HashSet<>(); // 이 게시물에 연결된 Tag 레코드들

    // 편의 메서드
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.setPost(this); // 태그 레코드에 이 게시물을 연결
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setPost(null); // 태그 레코드에서 이 게시물 연결 해제
    }

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}