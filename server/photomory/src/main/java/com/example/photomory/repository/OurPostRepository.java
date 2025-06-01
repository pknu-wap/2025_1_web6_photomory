package com.example.photomory.repository;

import com.example.photomory.entity.OurPost; // OurPost 엔티티 임포트
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // 스프링이 이 인터페이스를 리포지토리로 인식하도록 합니다.
public interface OurPostRepository extends JpaRepository<OurPost, Integer> {

    /**
     * @param albumId OurAlbum의 ID
     * @param pageable 페이징 정보 (페이지 번호, 페이지 크기, 정렬 등)
     * @return 페이징된 OurPost 목록
     */
    Page<OurPost> findByOurAlbum_AlbumId(Integer albumId, Pageable pageable); // <--- 'Album' -> 'OurAlbum'으로 변경!

    /**
     * 특정 앨범(OurAlbum)에 속한 모든 게시물들을 리스트 형태로 조회
     * OurPost 엔티티에 'ourAlbum' 필드가 OurAlbum을 참조하도록 설정되어 있어야 합니다.
     * @param albumId OurAlbum의 ID
     * @return OurPost 목록
     */
    List<OurPost> findByOurAlbum_AlbumId(Integer albumId); // <--- 'Album' -> 'OurAlbum'으로 변경!

    /**
     * @param postId 조회할 OurPost의 ID
     * @return 해당 ID를 가진 OurPost (Optional로 감싸져 있어 null일 수 있음)
     */
    Optional<OurPost> findById(Integer postId);

}