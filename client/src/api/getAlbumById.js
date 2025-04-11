import groupAlbums from "./groupAlbums.json";

// 앨범 ID로 해당 앨범 객체를 반환하는 함수
export default function getAlbumById(albumId) {
  if (!albumId) return null;

  for (const group of groupAlbums) {
    const album = group.albums.find((a) => a.id === albumId);
    if (album) {
      return {
        album,
        description: album.description, //앨범에 대한 설명
        groupMembers: group.members, //그룹 멤버
        groupName: group.groupName, //그룹 이름
      };
    }
  }

  return null;
}
