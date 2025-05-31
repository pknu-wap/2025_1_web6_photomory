package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;  // Set 구현체로 HashSet 사용 시 필요


import java.time.LocalDateTime;
import java.util.ArrayList; // List 초기화를 위해 필요
import java.util.List;

@Entity
@Table(name = "OurPost")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OurPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private OurAlbum ourAlbum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "post_text", length = 500)
    private String postText;

    @Column(name = "making_time", nullable = false)
    private LocalDateTime makingTime;

    @OneToMany(mappedBy = "ourPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "ourPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    // 여기에 tags 필드 추가
    @ManyToMany
    @JoinTable(
            name = "our_post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
}
