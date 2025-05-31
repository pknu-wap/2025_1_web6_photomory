// 나만의 추억 페이지 필드명 변환 함수 (정규화 함수)
export function normalizeMyAlbumData(myAlbum) {
  return {
    album_id: myAlbum.myalbumId,
    album_name: myAlbum.myalbumName,
    album_description: myAlbum.myalbumDescription,
    album_makingtime: myAlbum.myalbumMakingtime,
    photos: myAlbum.myphotos.map((p) => ({
      photo_id: p.myphotoId,
      photo_name: p.myphotoName,
      photo_url: p.myphotoUrl,
      photo_makingtime: p.myphotoMakingtime,
    })),
    tags: myAlbum.mytags,
  };
}

// 우리의 추억 페이지 필드명 변환 함수 (정규화 함수)
export function normalizeOurAlbumData(rawData) {
  return rawData.map((group) => ({
    group_id: group.groupId,
    group_name: group.groupName,
    members: group.members.map((m) => ({
      user_id: m.userId,
      user_name: m.userName,
      user_photourl: m.userPhotourl,
    })),
    albums: group.albums.map((a) => ({
      album_id: a.albumId,
      album_name: a.albumName,
      album_description: a.albumDescription,
      album_tag: Array.isArray(a.albumTag) ? a.albumTag : [a.albumTag], //항상 배열로 유지
      album_makingtime: a.albumMakingtime,
      photos: a.photos, // 그대로 유지
      comments: a.comments, // 그대로 유지
    })),
  }));
}

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
