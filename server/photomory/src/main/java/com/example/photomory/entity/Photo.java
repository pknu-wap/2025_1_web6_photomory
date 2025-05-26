package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "photo")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @Column(nullable = false)
    private String photoUrl;

    @Column(nullable = false)
    private String photoName;

    @Column(nullable = false)
    private String photoComment;

    @Column(nullable = false)
    private LocalDateTime photoMakingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    //hw..
    private String title;

    @Column(name = "`date`") // 예약어일 경우 백틱 처리
    private LocalDate date;

    private Long userId;


    public void setPost(Post post) {
        this.post = post;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public void setPhotoComment(String photoComment) {
        this.photoComment = photoComment;
    }

    public void setPhotoMakingTime(LocalDateTime photoMakingTime) {
        this.photoMakingTime = photoMakingTime;
    }

    //hw
    public Photo(String title, LocalDate date, String photoUrl) {
        this.title = title;
        this.date = date;
        this.photoUrl = photoUrl;
    }

    public Photo(String title, LocalDate date, String photoUrl, Long userId) {
        this.title = title;
        this.date = date;
        this.photoUrl = photoUrl;
        this.userId = userId;
    }
}