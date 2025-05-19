package com.example.photomory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor


public class ImageEntity {

    //2차-날짜,제목 추가함
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String date;
    private String imageUrl;

    public ImageEntity(String imageUrl, String title, String date) {

        this.imageUrl = imageUrl;
        this.title = title;
        this.date = date;

    }
}
