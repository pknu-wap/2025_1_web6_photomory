package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "post_text", nullable = false)
    private String postText;

    @Column(name = "likes_count", nullable = false)
    private Integer likesCount;

    @Column(name = "post_description", nullable = false)
    private String postDescription;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "post_making_time", nullable = false)
    private LocalDateTime postMakingTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    // 한 게시물에 여러 사진이 있을 경우
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Photo> photos;

    // 한 게시물에 여러 태그가 있을 경우
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tag> tags;

    // 한 게시물에 여러 댓글이 있을 경우
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @Column(length = 1000)
    private String photoUrl;

}
