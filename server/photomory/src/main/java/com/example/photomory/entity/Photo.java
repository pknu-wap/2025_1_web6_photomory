package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Photo") // <-- 테이블명은 "Photo"로 유지합니다. (이전 지시 철회)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer photoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "our_post_id", nullable = true)
    private OurPost ourPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_post_id", nullable = true)
    private MyPost myPost; // <--- MyPost 엔티티를 참조하는 필드 추가

    @Column(name = "photo_url", nullable = false, length = 500)
    private String photoUrl;

    @Column(name = "photo_name", length = 255)
    private String photoName;

    @Column(name = "photo_making_time")
    private LocalDateTime photoMakingTime;

    @Column(name = "title", length = 255)
    private String title;
}