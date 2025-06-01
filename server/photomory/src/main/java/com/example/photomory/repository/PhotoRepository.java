package com.example.photomory.repository;

import com.example.photomory.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.photomory.entity.OurPost;
import com.example.photomory.entity.MyPost;
import com.example.photomory.entity.EveryPost;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findAllByOurPost(OurPost ourPost);
    List<Photo> findAllByMyPost(MyPost myPost);
    List<Photo> findAllByEveryPost(EveryPost everyPost);

}
