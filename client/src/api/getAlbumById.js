import groupAlbums from "./groupAlbums.json";

// 앨범 ID로 해당 앨범 객체를 반환하는 함수
export default function getAlbumById(albumId) {
  if (!albumId) return null;

  for (const group of groupAlbums) {
    const album = group.albums.find((a) => a.id === albumId);
    if (album) {
      return {
        album,
        groupId: group.id, // 그룹 정보도 같이 반환하면 유용할 수 있음
        groupName: group.groupName,
      };
    }
  }

  return null;
}
