package com.example.photomory.repository;

import com.example.photomory.entity.MyAlbum;
import com.example.photomory.entity.MyPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MyPhotoRepository extends JpaRepository<MyPhoto, Integer> {

    List<MyPhoto> findByMyalbum(MyAlbum myalbum);
}
