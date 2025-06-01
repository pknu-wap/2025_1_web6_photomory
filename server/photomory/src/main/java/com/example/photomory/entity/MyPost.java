package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime; // LocalDateTime은 사용되지 않지만, 다른 엔티티에서 필요할 수 있으므로 유지
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MyPost")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "post_text", nullable = false, length = 250)
    private String postText;

    @Column(name = "post_description", nullable = false, length = 250)
    private String postDescription;

    @Column(name = "location", nullable = false, length = 250)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private MyAlbum myAlbum;

    @OneToMany(mappedBy = "myPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Photo> photos = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "my_post_tag",
            joinColumns = @JoinColumn(name = "my_post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
}
