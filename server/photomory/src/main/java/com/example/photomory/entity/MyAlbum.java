package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "MYALBUM")
@Getter
@Setter
@NoArgsConstructor
public class MyAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer myalbumId;

    private String myalbumName;

    @Column(name = "myalbum_tag")
    private String myalbumTag; // 예: "자연,풍경,감성"

    private LocalDateTime myalbumMakingtime;

    private String myalbumDescription;

    private Long userId;

    // 생성 시점 자동 설정(옵션)
    @PrePersist
    public void prePersist() {
        if (myalbumMakingtime == null) {
            myalbumMakingtime = LocalDateTime.now();
        }
    }
}
