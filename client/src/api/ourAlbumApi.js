const BASE_URL = process.env.REACT_APP_API_URL;

//그룹 생성 api함수
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

    const result = await response.json(); // 예: { myalbumId: 1, ... }
    return result;
  } catch (error) {
    console.error("그룹 생성 중 오류 발생:", error);
    throw error;
  }
}
