package com.example.photomory.repository;

import com.example.photomory.entity.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EveryPostRepository extends CrudRepository<Post, Long> {

    @Query("SELECT DISTINCT p FROM Post p " +
            "JOIN FETCH p.user u " +
            "LEFT JOIN FETCH p.tags t " +
            "LEFT JOIN FETCH p.comments c " +
            "LEFT JOIN FETCH c.user cu")
    List<Post> findAllWithUserAndComments();
}
