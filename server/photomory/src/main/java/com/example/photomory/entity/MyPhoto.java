package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "MYPHOTO")
@Getter
@Setter
@NoArgsConstructor
public class MyPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer myphotoId;

    private String myphotoUrl;

    private String myphotoName;

    private String mycomment;

    private LocalDateTime myphotoMakingtime;

    @ManyToOne
    @JoinColumn(name = "myalbum_id")
    private MyAlbum myalbum;
}
