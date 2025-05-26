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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = true)
    private Album album;

    @Column(name = "comment_text", length = 500)
    private String commentText;

    @Column(name = "comment_time", nullable = false) 
    private LocalDateTime commentTime; 
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tag> tags = new HashSet<>();

    // 편의 메서드
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.setComment(this); // 태그 레코드에 이 댓글을 연결
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setComment(null); // 태그 레코드에서 이 댓글 연결 해제
    }

    @PrePersist
    public void prePersist() {
        this.commentTime = LocalDateTime.now();
    }

}
