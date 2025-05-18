package com.example.photomory.repository;

import com.example.photomory.entity.AlbumMembers;
import com.example.photomory.entity.AlbumMembers.AlbumMembersId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumMembersRepository extends JpaRepository<AlbumMembers, AlbumMembersId> {

    // 유저ID와 마이앨범ID로 존재 여부 확인
    boolean existsByUserEntityUserIdAndMyAlbumMyalbumId(Integer userId, Integer myalbumId);

}
