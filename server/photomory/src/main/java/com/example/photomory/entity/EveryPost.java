package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet; // ★★★ 이 줄 추가 ★★★
import java.util.Set;

@Entity
@Table(name = "every_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EveryPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "post_text", nullable = false, length = 250)
    private String postText;

    @Column(name = "post_description", nullable = false, length = 250)
    private String postDescription;

    @Column(name = "location", nullable = false, length = 250)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = true)
    private EveryAlbum everyAlbum;

    @OneToMany(mappedBy = "everyPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Photo> photos = new HashSet<>();

    @OneToMany(mappedBy = "everyPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Comment> comments = new HashSet<>();

    @Column(name = "likes_count", nullable = false)
    @Builder.Default
    private Integer likesCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "making_time")
    private LocalDateTime makingTime;

    @ManyToMany
    @JoinTable(
            name = "every_post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    @Column(name = "comment_count", nullable = false)
    @Builder.Default
    private Integer commentCount = 0;
}