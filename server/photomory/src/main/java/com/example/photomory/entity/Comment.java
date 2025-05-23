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
@Table(name = "comment")
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
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // Comment가 Album에 직접 연결될 수 있도록 추가 (DTO의 albumId를 위함)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id") // DB의 comment 테이블에 album_id 컬럼이 있어야 합니다.
    private Album album;

    @Column(name = "comment_text", length = 500)
    private String commentsText; // DTO와 필드명 일치

    @Column(name = "comment_time", nullable = false) // DB 컬럼명과 일치
    private LocalDateTime commentTime; // DTO와 타입 일치

    // Comment와 Tag의 One-to-Many 관계
    // Tag 엔티티의 'comment' 필드(ManyToOne)에 의해 매핑됩니다.
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tag> tags = new HashSet<>(); // 이 댓글에 연결된 Tag 레코드들

    // 편의 메서드
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.setComment(this); // 태그 레코드에 이 댓글을 연결
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setComment(null); // 태그 레코드에서 이 댓글 연결 해제
    }
}