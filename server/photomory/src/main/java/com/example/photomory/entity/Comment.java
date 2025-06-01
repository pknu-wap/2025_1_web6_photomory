package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "every_post_id", nullable = true)
    private EveryPost everyPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "our_post_id", nullable = true)
    private OurPost ourPost;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "every_album_id", nullable = true)
    private EveryAlbum everyAlbum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "our_album_id", nullable = true)
    private OurAlbum ourAlbum;

    @Column(name = "comment_text", nullable = false, length = 500)
    private String commentText;

    @Column(name = "comment_time", nullable = false)
    private LocalDateTime commentTime;

    @PrePersist @PreUpdate
    public void validateParentRelationship() {
        int parentCount = 0;
        if (everyPost != null) parentCount++;
        if (ourPost != null) parentCount++;
        // if (myPost != null) parentCount++;

        if (everyAlbum != null) parentCount++;
        if (ourAlbum != null) parentCount++;

        if (parentCount != 1) {
            throw new IllegalStateException("댓글은 정확히 하나의 게시물 또는 앨범에만 연결되어야 합니다.");
        }

        if (commentTime == null) {
            commentTime = LocalDateTime.now();
        }
    }
}