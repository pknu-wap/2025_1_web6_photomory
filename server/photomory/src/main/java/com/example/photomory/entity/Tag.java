package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "tag_name", nullable = false)
    private String tagName;

    // Post와 ManyToMany 관계 유지
    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    // Comment와 ManyToOne 관계 추가 (null 허용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    // 생성자
    public Tag(String tagName) {
        this.tagName = tagName;
    }
}
