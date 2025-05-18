package com.example.photomory.repository;

import com.example.photomory.entity.AlbumMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumMembersRepository extends JpaRepository<AlbumMembers, Long> {

    // 유저ID와 그룹ID(마이앨범ID)로 존재 여부 체크
    boolean existsByUserUserIdAndMyAlbumMyalbumId(Integer userId, Integer myalbumId);

}
