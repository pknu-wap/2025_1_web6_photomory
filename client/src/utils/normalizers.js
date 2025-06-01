// 나만의 추억 페이지 필드명 변환 함수 (정규화 함수)
export function normalizeMyAlbumData(rawAlbums) {
  return rawAlbums.map((album) => ({
    album_id: album.myalbumId,
    album_name: album.myalbumName,
    album_description: album.myalbumDescription,
    album_makingtime: album.myalbumMakingtime,
    photos: (album.myphotos || []).map((p) => ({
      photo_id: p.myphotoId,
      photo_name: p.myphotoName,
      photo_url: p.myphotoUrl,
      photo_makingtime: p.myphotoMakingtime,
    })),
    tags: album.mytags || [],
  }));
}

// 우리의 추억 페이지 필드명 변환 함수 (정규화 함수)
export function normalizeOurAlbumData(rawData) {
  return rawData.map((group) => ({
    group_id: group.groupId,
    group_name: group.groupName,
    members:
      group.members?.map((m) => ({
        user_id: m.userId,
        user_name: m.userName,
        user_photourl: m.userPhotourl,
      })) ?? [],
    albums:
      group.albums?.map((a) => ({
        album_id: a.albumId,
        album_name: a.albumName,
        album_description: a.albumDescription,
        album_tag: a.albumTag
          ? a.albumTag.split(",").map((tag) => tag.trim())
          : [], //태그 배열 처리
        album_makingtime: a.albumMakingtime,
        photos:
          a.photos?.map((p) => ({
            photo_id: p.photoId,
            photo_url: p.photoUrl,
            photo_name: p.photoName,
            photo_makingtime: p.photoMakingtime,
            post_id: p.postId,
          })) ?? [],
        comments: a.comments ?? [],
      })) ?? [],
  }));
}

//앨범 생성 시 앨범 상태 정규화 함수
export function normalizeGroupAlbum(apiAlbum) {
  return {
    album_id: apiAlbum.albumId,
    album_name: apiAlbum.albumName,
    album_description: apiAlbum.albumDescription,
    album_tag: Array.isArray(apiAlbum.albumTags)
      ? apiAlbum.albumTags
      : apiAlbum.albumTags
      ? [apiAlbum.albumTags]
      : [],
    album_makingtime: apiAlbum.albumMakingTime?.slice(0, 10),
    photos: [], // API에서 포함되지 않으면 기본값
    comments: [], // 마찬가지
  };
}

//우리의 추억 상세 페이지 필드명 변환 함수 (정규화 함수)
export function normalizeGroupAlbumDetail(apiData, groupInfo) {
  return {
    album: {
      album_id: apiData.albumId,
      album_name: apiData.albumName,
      album_tag: Array.isArray(apiData.albumTags) ? apiData.albumTags : [],
      album_makingtime: apiData.albumMakingTime?.slice(0, 10),
      photos: apiData.posts ?? [],
    },
    description: apiData.albumDescription,
    groupName: groupInfo?.groupName ?? "", // groupInfo가 없으면 빈 문자열
    groupMembers:
      groupInfo?.members?.map((m) => ({
        user_id: m.userId,
        user_name: m.username,
        user_photourl: m.profileImageUrl,
      })) ?? [],
  };
}

export function normalizeMember(member) {
  return {
    user_id: member.userId,
    user_name: member.username,
    user_photourl: member.profileImageUrl,
  };
}
