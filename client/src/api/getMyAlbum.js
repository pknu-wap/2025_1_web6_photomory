import my_album_data_with_updated_photo from "./my_album_data_with_updated_photo_urls.json";
import { normalizeMyAlbumData } from "./normalizers";

// 정규화 후 리턴
export function getMyAlbums() {
  return my_album_data_with_updated_photo.map(normalizeMyAlbumData);
}
