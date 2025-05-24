package com.example.photomory.entity;

import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToMany(mappedBy = "tags")
    private Set<Album> albums = new HashSet<>();

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public Tag(String tagName, Post post, Comment comment) {
        this.tagName = tagName;
        this.post = post;
        this.comment = comment;
    }
}
