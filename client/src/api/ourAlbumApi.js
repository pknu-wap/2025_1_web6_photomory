const BASE_URL = process.env.REACT_APP_API_URL;

//우리의 추억 그룹 생성 api함수
export async function createGroup({ groupName, groupDescription }) {
  const token = localStorage.getItem("accessToken"); // 인증이 필요하다면 사용

  try {
    const response = await fetch(`${BASE_URL}/api/our-album/group`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`, // 필요 없다면 제거해도 됨
      },
      body: JSON.stringify({
        groupName,
        groupDescription,
      }),
    });

    if (!response.ok) {
      throw new Error("그룹 생성 실패: " + response.status);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("그룹 생성 중 오류 발생:", error);
    throw error;
  }
}

//우리의 추억 그룹 정보 불러오기 api함수
export async function fetchGroupInfo(groupId) {
  const token = localStorage.getItem("accessToken"); // 필요시 사용

  try {
    const response = await fetch(`${BASE_URL}/api/our-album/group/${groupId}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error("그룹 정보 불러오기 실패: " + response.status);
    }

    const data = await response.json();
    return data; // 그룹 정보 + members 배열
  } catch (error) {
    console.error("그룹 조회 중 에러:", error);
    throw error;
  }
}

//우리의 추억 앨범 생성 api 함수
export async function createGroupAlbum(
  groupId,
  { albumName, albumTag, albumMakingTime, albumDescription }
) {
  const token = localStorage.getItem("accessToken");

  try {
    const response = await fetch(
      `${BASE_URL}/api/our-album/group/${groupId}/album`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          albumName,
          albumTag,
          albumMakingTime,
          albumDescription,
        }),
      }
    );

    if (!response.ok) {
      throw new Error("앨범 생성 실패: " + response.status);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("앨범 생성 중 오류 발생:", error);
    throw error;
  }
}
