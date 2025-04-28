package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "album_connect")
@Getter
@NoArgsConstructor
public class AlbumConnect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "albumconnect_id")
    private Long id;

    @Column(name = "album_id", nullable = false)
    private Long albumId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public AlbumConnect(Long albumId, Long userId) {
        this.albumId = albumId;
        this.userId = userId;
    }
}
