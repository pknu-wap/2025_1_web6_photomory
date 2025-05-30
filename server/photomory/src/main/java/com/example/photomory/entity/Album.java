package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ALBUM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Integer albumId;

    @Column(name = "album_name", nullable = false)
    private String albumName;

    @Column(name = "album_tag")
    private String albumTag;

    @Column(name = "album_makingtime", nullable = false)
    private LocalDateTime albumMakingTime;

    @Column(name = "album_description", nullable = false)
    private String albumDescription;

    // album 테이블의 post_id 컬럼에 매핑되는 필드 (대표 포스트 등 용도)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post mainPost;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "myalbum_id")
    private MyAlbum myAlbum;

    @ManyToMany
    @JoinTable(
            name = "album_tag",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public void addPost(Post post) {
        this.posts.add(post);
        if (post.getAlbum() != this) {
            post.setAlbum(this);
        }
    }
}
