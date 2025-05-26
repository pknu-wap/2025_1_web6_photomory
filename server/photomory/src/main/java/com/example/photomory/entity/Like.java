package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LIKES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likes_id;  // 기존 PK 없으면 추가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "comment_id", nullable = false)
    private Integer commentId = 0; // 게시글 좋아요만 할 거니까 항상 0

    @Column(name = "likes_count", nullable = false)
    private Integer likesCount = 0;
}
