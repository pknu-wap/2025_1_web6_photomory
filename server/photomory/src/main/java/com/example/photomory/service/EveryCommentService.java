package com.example.photomory.service;

import com.example.photomory.dto.CommentRequestDto;
import com.example.photomory.entity.Album;
import com.example.photomory.entity.Comment;
import com.example.photomory.repository.AlbumRepository;
import com.example.photomory.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EveryCommentService {

    private final CommentRepository commentRepository;
    private final AlbumRepository albumRepository;

    public void addComment(CommentRequestDto dto) {
        Album album = albumRepository.findById(dto.getAlbumId())
                .orElseThrow(() -> new IllegalArgumentException("해당 앨범이 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.setAlbumId(dto.getAlbumId());
        comment.setPostId(dto.getPostId());
        comment.setAlbum(album);
        comment.setUserId(dto.getUserId());
        comment.setCommentsText(dto.getCommentsText());
        comment.setCommentCount(1);

        commentRepository.save(comment);
    }
}
