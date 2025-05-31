package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "Photo")
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
    private MyPost myPost;

    // ★★★ 이 부분을 추가합니다: EveryPost와의 관계 ★★★
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "every_post_id", nullable = true) // DB 컬럼명
    private EveryPost everyPost; // 필드명은 EveryPostService에서 사용하려는 'everyPost'로

    @Column(name = "photo_url", nullable = false, length = 500)
    private String photoUrl;

    @Column(name = "photo_name", length = 255)
    private String photoName;

    @Column(name = "photo_making_time")
    private LocalDateTime photoMakingTime;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "photo_date")
    private LocalDate date;

    public Long getUserId() {
        if (ourPost != null && ourPost.getUser() != null) {
            return ourPost.getUser().getUserId();
        } else if (myPost != null && myPost.getUser() != null) {
            return myPost.getUser().getUserId();
        } else if (everyPost != null && everyPost.getUser() != null) { // EveryPost 추가
            return everyPost.getUser().getUserId();
        }
        return null;
    }

    public LocalDate getDate() {
        return date;
    }

}