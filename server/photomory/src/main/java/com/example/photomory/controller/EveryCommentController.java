package com.example.photomory.controller;

import com.example.photomory.dto.CommentRequestDto;
import com.example.photomory.dto.EveryCommentDto;
import com.example.photomory.entity.Comment;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.entity.Post;
import com.example.photomory.entity.Album;
import com.example.photomory.repository.EveryCommentRepository;
import com.example.photomory.repository.UserRepository;
import com.example.photomory.repository.PostRepository;
import com.example.photomory.repository.AlbumRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/every/comments")
public class EveryCommentController {

    @Autowired
    private EveryCommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AlbumRepository albumRepository;

    // 댓글 조회
    @GetMapping("/{postId}")
    public List<EveryCommentDto> getCommentsByPostId(@PathVariable Integer postId) {
        List<Comment> comments = commentRepository.findByPost_PostId(postId);
        return comments.stream().map(comment -> {
            UserEntity user = comment.getUser(); // user 직접 가져오기
            String userName = (user != null) ? user.getUserName() : "알 수 없음";
            return EveryCommentDto.from(comment, userName);
        }).collect(Collectors.toList());
    }

    // 댓글 작성
    @PostMapping
    public void addComment(@RequestBody CommentRequestDto dto) {
        UserEntity user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("유저 없음"));
        Post post = postRepository.findById(dto.getPostId()).orElseThrow(() -> new RuntimeException("게시글 없음"));
        Album album = albumRepository.findById(dto.getAlbumId()).orElse(null); // optional

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .album(album)
                .commentsText(dto.getCommentsText())
                .commentTime(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
    }
}
