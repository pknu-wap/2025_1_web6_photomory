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
    const response = await fetch("/api/my-albums", {
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
    const response = await fetch("/api/my-albums/all", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (!response.ok) {
      throw new Error(`서버 응답 오류: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("앨범 조회 실패:", error);
    throw error;
  }
}

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
    const response = await fetch(`/api/my-albums/${albumId}`, {
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
