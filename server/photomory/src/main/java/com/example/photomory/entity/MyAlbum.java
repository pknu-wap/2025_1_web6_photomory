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
    private String myalbumTag; // "자연,풍경,감성" 형식으로 저장

    private LocalDateTime myalbumMakingtime;

    private String myalbumDescription;

    private Integer userId;
}
