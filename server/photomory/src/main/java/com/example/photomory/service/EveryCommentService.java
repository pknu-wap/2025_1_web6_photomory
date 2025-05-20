package com.example.photomory.service;

import com.example.photomory.dto.CommentRequestDto;
import com.example.photomory.entity.Album;
import com.example.photomory.entity.Comment;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.AlbumRepository;
import com.example.photomory.repository.CommentRepository;
import com.example.photomory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EveryCommentService {

    private final CommentRepository commentRepository;
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;

    public void addComment(CommentRequestDto dto) {
        Integer albumId = dto.getAlbumId();      // 이미 Integer면 그냥 쓰기
        Integer postId = dto.getPostId();
        Long userId = dto.getUserId();           // Long 타입이니까 그냥 그대로

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new IllegalArgumentException("해당 앨범이 존재하지 않습니다."));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Comment comment = new Comment();
        comment.setAlbumId(albumId);
        comment.setPostId(postId);
        comment.setAlbum(album);
        comment.setUser(user);
        comment.setCommentsText(dto.getCommentsText());

        commentRepository.save(comment);
    }

}
