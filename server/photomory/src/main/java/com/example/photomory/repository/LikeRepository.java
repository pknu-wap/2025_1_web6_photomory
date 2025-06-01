package com.example.photomory.repository;

import com.example.photomory.entity.Like;
import com.example.photomory.entity.UserEntity;
import com.example.photomory.entity.OurPost;
import com.example.photomory.entity.MyPost;
import com.example.photomory.entity.EveryPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndOurPost(UserEntity user, OurPost ourPost);

    Optional<Like> findByUserAndMyPost(UserEntity user, MyPost myPost);

    Optional<Like> findByUserAndEveryPost(UserEntity user, EveryPost everyPost);

    Long countByOurPost(OurPost ourPost);

    Long countByMyPost(MyPost myPost);

    Long countByEveryPost(EveryPost everyPost);

    void deleteByUserAndOurPost(UserEntity user, OurPost ourPost);

    void deleteByUserAndMyPost(UserEntity user, MyPost myPost);

    void deleteByUserAndEveryPost(UserEntity user, EveryPost everyPost);
}
