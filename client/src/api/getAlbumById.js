import final_group_album_data from "./final_group_album_data";
import final_my_album_data from "./final_my_album_data";

// album_id + type을 받아서 앨범 찾아오는 함수
export default function getAlbumById(album_id, type, group_id = null) {
  if (!album_id || !type) return null;
  //그룹의 앨범을 그룹id, 앨범 아이디를 이용해서 찾기
  if (type === "group") {
    const group = final_group_album_data.find((g) => g.group_id === group_id);
    if (!group) return null; // groupId가 진짜 없을 경우만 null 반환

    const album = group.albums.find((a) => a.album_id === album_id);

    return {
      album: album || null, // 앨범이 없으면 null
      description: album?.album_description || null,
      groupMembers: group.members,
      groupName: group.group_name,
    };
  }
  //개인 앨범을 id를 이용해서 찾기
  else if (type === "private") {
    const myAlbum = final_my_album_data.find((a) => a.album_id === album_id);
    if (myAlbum) {
      return {
        album: myAlbum,
        description: myAlbum.album_description,
      };
    }
  }

  return null; // 둘 다 없으면 NULL 반환
}
