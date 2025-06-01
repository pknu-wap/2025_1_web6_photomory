package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "OUR_ALBUM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OurAlbum {

    @OneToMany(mappedBy = "ourAlbum", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Comment> comments = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Integer albumId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "album_name", nullable = false, length = 45)
    private String albumName;

    @Column(name = "album_tag", length = 45)
    private String albumTag;

    @Column(name = "album_makingtime", nullable = false)
    private LocalDateTime albumMakingTime;

    @Column(name = "album_description", nullable = false, length = 250)
    private String albumDescription;

    @OneToMany(mappedBy = "ourAlbum", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<OurPost> posts = new HashSet<>();
}
