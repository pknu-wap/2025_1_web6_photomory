package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList; // List 초기화를 위해 필요
import java.util.List;

@Entity
@Table(name = "OurPost") // 테이블 이름은 명시된 대로 유지합니다. (일반적으로는 "our_post"와 같이 소문자 스네이크 케이스를 사용합니다.)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OurPost { // 클래스 이름 OurPost 유지

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false) // 실제 DB의 외래 키 컬럼명
    private OurAlbum ourAlbum; // <--- 'album' 대신 'ourAlbum'으로 변경!

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 사용자 엔티티와의 관계
    private UserEntity user;

    @Column(name = "post_text", length = 500)
    private String postText;

    @Column(name = "making_time", nullable = false)
    private LocalDateTime makingTime;

    @OneToMany(mappedBy = "ourPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // <--- 'post' 대신 'ourPost'로 변경!
    @Builder.Default // Lombok @Builder를 사용할 때 초기화를 위해 필요
    private List<Photo> photos = new ArrayList<>(); // 초기화 (Lazy Loading)

    @OneToMany(mappedBy = "ourPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // <--- 'post' 대신 'ourPost'로 변경!
    @Builder.Default // Lombok @Builder를 사용할 때 초기화를 위해 필요
    private List<Comment> comments = new ArrayList<>(); // 초기화 (Lazy Loading)

}