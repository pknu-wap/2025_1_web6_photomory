package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Photo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer photoId;

    @Column(name = "post_id", nullable = false)
    private Integer postId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "our_post_id", nullable = true)
    private OurPost ourPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_post_id", nullable = true)
    private MyPost myPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "every_post_id", nullable = true)
    private EveryPost everyPost;

    @Column(name = "photo_url", nullable = false, length = 500)
    private String photoUrl;

    @Column(name = "photo_name", length = 255)
    private String photoName;

    @Column(name = "photo_making_time")
    private LocalDate photoMakingTime;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "photo_date")
    private LocalDate date;

    @Column(name = "post_type", nullable = false)
    private String postType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Long getUserId() {
        if (ourPost != null && ourPost.getUser() != null) {
            return ourPost.getUser().getUserId();
        } else if (myPost != null && myPost.getUser() != null) {
            return myPost.getUser().getUserId();
        } else if (everyPost != null && everyPost.getUser() != null) {
            return everyPost.getUser().getUserId();
        }
        return null;
    }
}
