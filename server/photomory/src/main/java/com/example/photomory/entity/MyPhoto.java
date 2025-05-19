package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MYPHOTO")
@Getter @Setter
@NoArgsConstructor
public class MyPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer myphotoId;

    private String myphotoUrl;

    @ManyToOne
    @JoinColumn(name = "myalbum_id")
    private MyAlbum myalbum;
}
