package com.example.photomory.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ALBUM")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Integer albumId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false, referencedColumnName = "post_id")
    private Post post;

    @Column(name = "album_name", nullable = false)
    private String albumName;

    @Column(name = "album_tag")
    private String albumTag;

    @Column(name = "album_makingtime", nullable = false)
    private LocalDateTime albumMakingTime;

    @Column(name = "album_description", nullable = false)
    private String albumDescription;

    @OneToMany(mappedBy = "album")
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "myalbum_id") // FK 컬럼명 실제 DB에 맞게 조정 필요
    private MyAlbum myAlbum;

}
