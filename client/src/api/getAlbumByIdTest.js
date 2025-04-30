import final_group_album_data from "./final_group_album_data";
import final_my_album_data from "./final_my_album_data";

// album_id + type을 받아서 앨범 찾아오는 함수
export default function getAlbumByIdTest(album_id, type) {
  if (!album_id || !type) return null;
  //그룹의 앨범을 id를 이용해서 찾기
  if (type === "group") {
    for (const group of final_group_album_data) {
      const album = group.albums.find((a) => a.album_id === album_id);
      if (album) {
        return {
          album,
          description: album.album_description, //앨범 설명
          groupMembers: group.members, //앨범에 대한 그룹 멤버 들
          groupName: group.group_name, //그룹명
        };
      }
    }
    //개인 앨범을 id를 이용해서 찾기
  } else if (type === "private") {
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
