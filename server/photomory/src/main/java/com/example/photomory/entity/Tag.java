package com.example.photomory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "tag_name", nullable = false) // UNIQUE 제약 조건 제거됨 (post_id, comment_id와 함께 중복될 수 있음)
    private String tagName;

    // Post와의 ManyToOne 관계
    // DB의 tag 테이블에 post_id 컬럼이 있으므로 매핑
    @ManyToOne(fetch = FetchType.LAZY, optional = true) // post_id가 NULL을 허용하므로 optional = true
    @JoinColumn(name = "post_id") // DB 컬럼 이름과 일치
    private Post post; // 이 Tag 레코드가 연결된 Post (NULL 가능)

    // Comment와의 ManyToOne 관계
    // DB의 tag 테이블에 comment_id 컬럼이 있으므로 매핑
    @ManyToOne(fetch = FetchType.LAZY, optional = true) // comment_id가 NULL을 허용하므로 optional = true
    @JoinColumn(name = "comment_id") // DB 컬럼 이름과 일치
    private Comment comment; // 이 Tag 레코드가 연결된 Comment (NULL 가능)

    // Album과의 ManyToMany 관계
    // Album.java의 'albumTags' 필드에 의해 매핑됨
    @ManyToMany(mappedBy = "albumTags")
    private Set<Album> albums = new HashSet<>(); // 이 Tag가 연결된 앨범들 (post_id, comment_id가 NULL인 경우)


    public Tag(String tagName) {
        this.tagName = tagName;
    }

    // 새롭게 추가된 post, comment 필드를 포함하는 생성자 (필요에 따라)
    public Tag(String tagName, Post post, Comment comment) {
        this.tagName = tagName;
        this.post = post;
        this.comment = comment;
    }
}