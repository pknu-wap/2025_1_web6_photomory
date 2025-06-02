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
    @Column(name = "myalbum_id")
    private Integer myalbumId;

    @Column(name = "myalbum_name") // ⚠️ 수정: DB 컬럼명과 정확히 일치시킴
    private String myalbumName;

    @Column(name = "myalbum_tag")
    private String myalbumTag; // 예: "자연,풍경,감성"

    @Column(name = "album_makingtime")
    private LocalDateTime myalbumMakingtime;

    @Column(name = "myalbum_description")
    private String myalbumDescription;

    @Column(name = "user_id")
    private Long userId;

    @PrePersist
    public void prePersist() {
        if (myalbumMakingtime == null) {
            myalbumMakingtime = LocalDateTime.now();
        }
    }
}
