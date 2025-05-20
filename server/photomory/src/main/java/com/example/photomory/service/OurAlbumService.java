package com.example.photomory.service;

import com.example.photomory.dto.*;
import com.example.photomory.entity.Album;
import com.example.photomory.entity.Comment;
import com.example.photomory.entity.Post;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.repository.AlbumMembersRepository;
import com.example.photomory.repository.AlbumRepository;
import com.example.photomory.repository.CommentRepository;
import com.example.photomory.repository.FriendRepository;
import com.example.photomory.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OurAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMembersRepository albumMembersRepository;
    private final FriendRepository friendRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public OurAlbumService(AlbumRepository albumRepository,
                           AlbumMembersRepository albumMembersRepository,
                           FriendRepository friendRepository,
                           PostRepository postRepository,
                           CommentRepository commentRepository) {
        this.albumRepository = albumRepository;
        this.albumMembersRepository = albumMembersRepository;
        this.friendRepository = friendRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    public OurAlbumFullResponseDto getAlbumFullDetails(Integer albumId, Long requestUserId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));

        if (album.getPosts() == null || album.getPosts().isEmpty()) {
            throw new EntityNotFoundException("앨범에 게시물이 없습니다.");
        }

        Long albumOwnerUserId = album.getPosts().get(0).getUser().getUserId();

        boolean areFriends = friendRepository.existsByFromUserIdAndToUserId(requestUserId, albumOwnerUserId);
        if (!areFriends) {
            throw new SecurityException("친구가 아니면 접근할 수 없습니다.");
        }

        boolean isGroupMember = albumMembersRepository.existsByUserEntityUserIdAndMyAlbumMyalbumId(
                requestUserId.intValue(), album.getMyAlbum().getMyalbumId());
        if (!isGroupMember) {
            throw new SecurityException("그룹 멤버만 접근할 수 있습니다.");
        }

        List<Post> posts = postRepository.findByAlbum_AlbumId(albumId);
        List<PostWithCommentsResponseDto> postDtos = new ArrayList<>();

        for (Post post : posts) {
            List<Comment> comments = commentRepository.findByAlbum_AlbumIdAndPost_PostId(albumId, post.getPostId());
            postDtos.add(PostWithCommentsResponseDto.fromEntity(post, comments));
        }

        return OurAlbumFullResponseDto.from(album, postDtos);
    }

    @Transactional
    public CommentResponseDto createComment(Integer albumId, Integer postId, UserEntity user, String text) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException("앨범을 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setAlbum(album);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCommentsText(text);

        Comment saved = commentRepository.save(comment);
        return CommentResponseDto.fromEntity(saved);
    }
}
