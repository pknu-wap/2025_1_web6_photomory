package com.example.photomory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    @NotNull(message = "앨범 ID는 필수입니다.")
    private Integer albumId;

    @NotNull(message = "게시글 ID는 필수입니다.")
    private Integer postId;

    @NotBlank(message = "댓글 내용은 비워둘 수 없습니다.")
    @Size(max = 500, message = "댓글 내용은 500자를 초과할 수 없습니다.")
    private String commentsText;
}