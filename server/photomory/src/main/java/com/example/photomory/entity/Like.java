package com.example.photomory.entity;

import com.example.photomory.entity.OurPost;
import com.example.photomory.entity.MyPost;
import com.example.photomory.entity.EveryPost;
import com.example.photomory.entity.UserEntity;

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
    private Long likesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // OurPost 좋아요용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "our_post_id")
    private OurPost ourPost;

    // MyPost 좋아요용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_post_id")
    private MyPost myPost;

    // EveryPost 좋아요용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "every_post_id")
    private EveryPost everyPost;

    @Column(name = "comment_id", nullable = false)
    private Integer commentId = 0; // 게시글 좋아요만 할 거니까 항상 0

    @Column(name = "likes_count", nullable = false)
    private Integer likesCount = 0;
}
