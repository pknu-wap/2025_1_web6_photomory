package com.example.photomory.repository;

import com.example.photomory.entity.MyAlbum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyAlbumRepository extends JpaRepository<MyAlbum, Integer> {

    List<MyAlbum> findByUserId(long userId);

}
