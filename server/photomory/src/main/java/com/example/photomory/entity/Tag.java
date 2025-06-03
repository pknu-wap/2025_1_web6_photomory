package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "post_type", nullable = false)
    private String postType;

    @Column(name = "post_id", nullable = false)
    private Integer postId;

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<EveryPost> everyPosts = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<OurPost> ourPosts = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<MyPost> myPosts = new HashSet<>();


    public void addEveryPost(EveryPost everyPost) {
        if (this.everyPosts == null) {
            this.everyPosts = new HashSet<>();
        }
        this.everyPosts.add(everyPost);
    }

    public void removeEveryPost(EveryPost everyPost) {
        if (this.everyPosts != null) {
            this.everyPosts.remove(everyPost);
        }
    }
}
