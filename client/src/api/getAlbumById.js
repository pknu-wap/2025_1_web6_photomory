import final_group_album_data from "./final_group_album_data";
import my_album_data_with_updated_photo from "./my_album_data_with_updated_photo_urls.json";
import { normalizeMyAlbumData } from "../utils/normalizers";

// album_id + type을 받아서 앨범 찾아오는 함수
export default function getAlbumById(album_id, type, group_id) {
  if (!type) return null;

  if (type === "group") {
    const group = final_group_album_data.find((g) => g.group_id === group_id);
    if (!group) return null;

    const album = group.albums.find((a) => a.album_id === album_id);

    return {
      album: album || null,
      description: album?.album_description || null,
      groupMembers: group.members,
      groupName: group.group_name,
    };
  }

  if (type === "private") {
    const normalizedAlbums =
      my_album_data_with_updated_photo.map(normalizeMyAlbumData);
    const myAlbum = normalizedAlbums.find((a) => a.album_id === album_id);
    if (myAlbum) {
      return {
        album: myAlbum,
        description: myAlbum.album_description,
      };
    }
  }

  return null;
}
