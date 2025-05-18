package com.example.photomory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.List;
import com.example.photomory.entity.Like;


@Entity
@Table(name = "POST")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "post_text", nullable = false)
    private String postText;

    @Column(name = "likes_count", nullable = false)
    private Integer likesCount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "post")
    private List<Tag> tags;

    @OneToMany(mappedBy = "post")
    private List<Photo> photos;

    @OneToMany(mappedBy = "post")
    private List<Like> likes;

    @OneToMany(mappedBy = "post")
    private List<Album> albums;

}