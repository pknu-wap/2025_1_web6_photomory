package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "POST")
@Getter
@NoArgsConstructor
public class EveryPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "post_text", nullable = false)
    private String postText;

    @Column(name = "post_description")
    private String postDescription;

    @Column(name = "location")
    private String location;

    @Column(name = "likes_count", nullable = false)
    private Integer likesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EveryPhoto> photos;
}