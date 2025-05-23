package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "album")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "album_name", nullable = false, length = 100)
    private String albumName;

    @Column(name = "album_description", length = 500)
    private String albumDescription;

    @Column(name = "album_making_time", nullable = false)
    private LocalDateTime albumMakingTime;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "album_tags",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> albumTags = new HashSet<>();

    // ========== 여기를 추가합니다. MyAlbum과의 관계 ==========
    // Album이 MyAlbum에 속한다면 ManyToOne 관계
    // MyAlbum 엔티티가 존재하고, DB의 album 테이블에 myalbum_id (또는 group_id) 컬럼이 있다고 가정합니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "myalbum_id") // DB의 album 테이블에 MyAlbum 엔티티의 ID를 참조하는 FK 컬럼 이름
    private MyAlbum myAlbum; // 'myAlbum' 필드 이름을 사용합니다.
    // ===================================================

    // 편의 메서드 (기존에 유지)
    public void addTag(Tag tag) {
        this.albumTags.add(tag);
        if (tag.getAlbums() != null) {
            tag.getAlbums().add(this);
        }
    }

    public void removeTag(Tag tag) {
        this.albumTags.remove(tag);
        if (tag.getAlbums() != null) {
            tag.getAlbums().remove(this);
        }
    }
}