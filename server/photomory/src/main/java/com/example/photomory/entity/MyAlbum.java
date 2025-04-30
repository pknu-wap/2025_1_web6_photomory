package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MYALBUM")
@Getter @Setter
@NoArgsConstructor
public class MyAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer myalbumId;

    private String myalbumName;

    private String myalbumTag;

    private LocalDateTime myalbumMakingtime;

    private String myalbumDescription;

    private Long userId;

    @OneToMany(mappedBy = "myalbum", cascade = CascadeType.ALL)
    private List<MyPhoto> photos = new ArrayList<>();
}
