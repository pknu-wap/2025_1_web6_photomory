package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "POST")
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

    @Column(name = "post_text")
    private String postText;

    @Column(name = "post_description")
    private String postDescription;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "location")
    private String location;

    @Column(name = "making_time")
    private LocalDateTime makingTime;

    @Column(name = "likes_count")
    private Integer likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "post_tag", // 중간 테이블 이름
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
}
