package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "photo")
@Getter
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    private String photoUrl;
    private String photoName;
    private String photoComment;
    private LocalDateTime photoMakingTime;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private MyAlbum myAlbum;
}
