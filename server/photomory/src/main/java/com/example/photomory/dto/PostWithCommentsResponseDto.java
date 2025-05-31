package com.example.photomory.dto;

import com.example.photomory.entity.Comment;
import com.example.photomory.entity.OurPost; // !!! 이 줄만 있어야 합니다. Post는 없어야 합니다. !!!
import com.example.photomory.entity.Photo; // Photo 엔티티 임포트 (필요시)
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostWithCommentsResponseDto {
    private Integer postId;
    private String postText;
    private LocalDateTime makingTime;
    private List<CommentResponseDto> comments;
    private List<PhotoResponseDto> photos; // 사진 정보를 위한 필드 (필요시)

    /**
     *
     * @param ourPost
     * @param comments 해당 게시물에 속한 Comment 리스트
     * @return PostWithCommentsResponseDto
     */
    public static PostWithCommentsResponseDto fromEntity(OurPost ourPost, List<Comment> comments) {
        List<CommentResponseDto> commentDtos = comments.stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());

        // OurPost 엔티티에서 Photo 정보를 가져와 DTO로 변환
        List<PhotoResponseDto> photoDtos = ourPost.getPhotos() != null ?
                ourPost.getPhotos().stream()
                        .map(PhotoResponseDto::fromEntity) // PhotoResponseDto.fromEntity가 Photo를 받도록 가정
                        .collect(Collectors.toList()) :
                List.of();

        return PostWithCommentsResponseDto.builder()
                .postId(ourPost.getPostId())
                .postText(ourPost.getPostText())
                .makingTime(ourPost.getMakingTime())
                .comments(commentDtos)
                .photos(photoDtos) // 사진 정보 포함
                .build();
    }
}