package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet; // ★★★ 이 줄 추가 ★★★
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

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<EveryPost> everyPosts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public void addEveryPost(EveryPost everyPost) {
        if (this.everyPosts == null) {
            this.everyPosts = new HashSet<>();
        }
        this.everyPosts.add(everyPost);
    }
    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<OurPost> ourPosts = new HashSet<>();

    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<MyPost> myPosts = new HashSet<>();


    public void removeEveryPost(EveryPost everyPost) {
        if (this.everyPosts != null) {
            this.everyPosts.remove(everyPost);
        }
    }

}