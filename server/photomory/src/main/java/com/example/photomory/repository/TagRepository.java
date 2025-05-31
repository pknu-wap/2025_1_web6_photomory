package com.example.photomory.repository;

import com.example.photomory.entity.Tag;
import com.example.photomory.entity.OurPost;
import com.example.photomory.entity.MyPost;
import com.example.photomory.entity.EveryPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Optional<Tag> findByTagName(String tagName);

    List<Tag> findByOurPostsContaining(OurPost ourPost);

    List<Tag> findByMyPostsContaining(MyPost myPost);

    List<Tag> findByEveryPostsContaining(EveryPost everyPost);
}
