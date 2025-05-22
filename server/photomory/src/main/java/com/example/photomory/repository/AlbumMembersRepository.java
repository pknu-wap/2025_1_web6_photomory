package com.example.photomory.repository;

import com.example.photomory.entity.AlbumMembers;
// import com.example.photomory.entity.AlbumMembers.AlbumMembersId; // AlbumMembersId를 직접 사용하지 않는다면 제거해도 됩니다.
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Optional 임포트 추가

@Repository
public interface AlbumMembersRepository extends JpaRepository<AlbumMembers, Long> { // AlbumMembersId 대신 Long으로 변경 (AlbumMembers 엔티티의 ID가 Long이라면)
    // 만약 AlbumMembers 엔티티의 @Id가 Long이 아니라 @EmbeddedId로 AlbumMembersId를 사용한다면 JpaRepository<AlbumMembers, AlbumMembersId>를 유지해야 합니다.
    // 하지만 대부분의 경우 단일 기본 키를 사용하므로 Long으로 가정합니다.
    // 만약 AlbumMembersId를 사용한다면 아래 메서드들의 파라미터 타입도 적절히 조정해야 합니다.
    // 현재 오류는 Integer/Long 타입 불일치이므로, MyAlbum의 myalbumId가 Integer이고 UserEntity의 userId가 Long인 것에 맞춰 수정합니다.


    /**
     * 특정 유저가 특정 그룹(마이앨범)에 속해 있는지 확인
     *
     * @param userId 유저 ID (Long)
     * @param myalbumId 그룹 ID (Integer)
     * @return 존재 여부
     */
    boolean existsByUserEntityUserIdAndMyAlbumMyalbumId(Long userId, Integer myalbumId); // userId를 Long으로 변경

    /**
     * 특정 그룹에 속한 모든 멤버 조회
     *
     * @param myalbumId 그룹 ID (Integer)
     * @return 해당 그룹의 모든 멤버 목록
     */
    List<AlbumMembers> findByMyAlbum_MyalbumId(Integer myalbumId);

    /**
     * 특정 유저가 속한 모든 그룹(마이앨범) 목록 조회
     *
     * @param userId 유저 ID (Long)
     * @return 유저가 속한 그룹들의 AlbumMembers 목록
     */
    List<AlbumMembers> findByUserEntityUserId(Long userId);


    // **아래 두 메서드는 'Cannot resolve method' 오류를 해결하기 위해 추가하는 메서드입니다.**

    /**
     * 특정 그룹의 특정 유저 ID에 해당하는 멤버를 조회하는 메서드
     * OurAlbumService.deleteGroupMember 등에서 사용됩니다.
     *
     * @param myalbumId 그룹 ID (Integer)
     * @param userId 유저 ID (Long)
     * @return 해당 멤버 엔티티 (Optional)
     */
    Optional<AlbumMembers> findByMyAlbum_MyalbumIdAndUserEntity_UserId(Integer myalbumId, Long userId);

    /**
     * 특정 그룹의 멤버 수를 세는 메서드
     * OurAlbumService.deleteGroupMember 등에서 사용됩니다.
     *
     * @param myalbumId 그룹 ID (Integer)
     * @return 해당 그룹의 멤버 수
     */
    long countByMyAlbum_MyalbumId(Integer myalbumId);
}