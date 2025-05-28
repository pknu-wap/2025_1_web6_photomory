import { normalizeMyAlbumData } from "../utils/normalizers"; // 정규화 함수 불러오기

const BASE_URL = process.env.REACT_APP_API_URL;

//나만의 추억 앨범 생성 api함수
export async function createMyMemoryAlbum({
  myalbumName,
  myalbumDescription,
  mytags,
}) {
  const accessToken = localStorage.getItem("accessToken");

  const bodyData = {
    myalbumName,
    myalbumDescription,
    mytags,
  };

  try {
    const response = await fetch(`${BASE_URL}/api/my-albums`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify(bodyData),
    });

    if (!response.ok) {
      throw new Error(`서버 응답 오류: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("앨범 생성 실패:", error);
    throw error;
  }
}

//나만의 추억 앨범 조회 api함수
export async function getMyMemoryAlbums() {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(`${BASE_URL}/api/my-albums/all`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (!response.ok) {
      throw new Error(`서버 응답 오류: ${response.status}`);
    }

    const result = await response.json();
    //정규화 처리
    const normalized = result.map(normalizeMyAlbumData);

    return [normalized];
  } catch (error) {
    console.error("앨범 조회 실패:", error);
    throw error;
  }
}

//나만의 추억 앨범 수정 api함수
export async function updateMyMemoryAlbum(
  albumId,
  myalbumName,
  myalbumDescription,
  mytags
) {
  const accessToken = localStorage.getItem("accessToken");

  const bodyData = {
    myalbumName,
    myalbumDescription,
    mytags, // 배열로 전달
  };

  try {
    const response = await fetch(`${BASE_URL}/api/my-albums/${albumId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify(bodyData),
    });

    if (!response.ok) {
      throw new Error(`수정 실패: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("앨범 수정 중 오류 발생:", error);
    throw error;
  }
}

//나만의 추억 앨범 삭제 api함수
export async function deleteMyMemoryAlbum(albumId) {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(`${BASE_URL}/api/my-albums/${albumId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (!response.ok) {
      throw new Error(`삭제 실패: ${response.status}`);
    }

    const result = await response.text(); // 서버에서 텍스트 응답인 경우
    return result;
  } catch (error) {
    console.error("앨범 삭제 중 오류 발생:", error);
    throw error;
  }
}

//나만의 추억 사진 추가 api함수
export async function addPhotosToMyAlbum(albumId, formData) {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(
      `${BASE_URL}/api/my-albums/${albumId}/photos`,
      {
        method: "PATCH",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
        body: formData,
      }
    );

    if (!response.ok) {
      throw new Error(`사진 추가 실패: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("사진 추가 중 오류:", error);
    throw error;
  }
}

//나만의 추억 사진 삭제 api함수
export async function deleteMyAlbumPhoto(photoId) {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(`/api/my-albums/photos/${photoId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (!response.ok) {
      throw new Error(`삭제 실패: ${response.status}`);
    }

    const result = await response.text(); // 서버가 문자열 응답하는 경우
    return result;
  } catch (error) {
    console.error("사진 삭제 중 오류 발생:", error);
    throw error;
  }
}
