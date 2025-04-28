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
//JPA 엔티티는 기본 생성자(@NoArgsConstructor)만 있으면 되고, id는 직접 넣지 않고 @GeneratedValue로 자동 생성된다!
//AllArgsConstuctor 때문에 이 사단이 났다!!

public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    public ImageEntity(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
