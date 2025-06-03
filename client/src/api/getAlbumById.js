import { fetchMyMemoryAlbums } from "../api/myAlbumAPi"; // 개인 앨범 전체 불러오기 API
import { normalizeMyAlbumData } from "../utils/normalizers"; // 정규화 함수

// album_id를 받아서 개인 앨범 중 하나를 찾아 리턴
export async function getMyAlbumById(album_id) {
  try {
    const rawAlbums = await fetchMyMemoryAlbums(); // 서버에서 전체 앨범 받아오기
    const normalizedAlbums = normalizeMyAlbumData(rawAlbums); // 앨범 데이터 정규화화

    //앨범 id를 통한 특정 앨범 조회
    const matchedAlbum = normalizedAlbums.find(
      (album) => album.album_id === parseInt(album_id)
    );

    if (!matchedAlbum) return null;

    return {
      album: matchedAlbum,
      description: matchedAlbum.album_description,
    };
  } catch (error) {
    console.error("개인 앨범 불러오기 실패:", error);
    return null;
  }
}
