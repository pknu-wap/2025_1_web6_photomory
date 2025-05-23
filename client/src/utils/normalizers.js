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
