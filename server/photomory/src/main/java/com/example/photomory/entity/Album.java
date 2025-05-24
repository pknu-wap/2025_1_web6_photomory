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
    @JoinColumn(name = "myalbum_id")
    private MyAlbum myAlbum;

    // Getter & Setter 추가

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumTag() {
        return albumTag;
    }

    public void setAlbumTag(String albumTag) {
        this.albumTag = albumTag;
    }

    public LocalDateTime getAlbumMakingTime() {
        return albumMakingTime;
    }

    public void setAlbumMakingTime(LocalDateTime albumMakingTime) {
        this.albumMakingTime = albumMakingTime;
    }

    public String getAlbumDescription() {
        return albumDescription;
    }

    public void setAlbumDescription(String albumDescription) {
        this.albumDescription = albumDescription;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public MyAlbum getMyAlbum() {
        return myAlbum;
    }

    public void setMyAlbum(MyAlbum myAlbum) {
        this.myAlbum = myAlbum;
    }
}
