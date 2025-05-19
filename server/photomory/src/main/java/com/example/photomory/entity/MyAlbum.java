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
@Getter
@Setter
@NoArgsConstructor
public class MyAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "myalbum_id")
    private Long myalbumId;

    @Column(name = "myalbum_name")
    private String myalbumName;

    @Column(name = "myalbum_tag")
    private String myalbumTag;

    @Column(name = "myalbum_makingtime")
    private LocalDateTime myalbumMakingtime;

    @Column(name = "myalbum_description")
    private String myalbumDescription;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "myalbum", cascade = CascadeType.ALL)
    private List<MyPhoto> photos = new ArrayList<>();
}
