import final_group_album_data from "./final_group_album_data";

// 앨범 ID로 해당 앨범 객체를 반환하는 함수
export default function getAlbumByIdTest(album_id) {
  if (!album_id) return null;

  for (const group of final_group_album_data) {
    const album = group.albums.find((a) => a.album_id === album_id);
    if (album) {
      return {
        album,
        description: album.album_description, //앨범에 대한 설명
        groupMembers: group.members, //그룹 멤버
        groupName: group.group_name, //그룹 이름
      };
    }
  }

  return null;
}
