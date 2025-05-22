//친구 요청 목록 불러오기 api함수수
export async function sentFriendRequests(userId) {
  const token = localStorage.getItem("accessToken"); // 또는 쿠키 등에서 가져옴

  try {
    const response = await fetch(`/api/friend-requests/sent?userId=${userId}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error("서버 응답 오류: " + response.status);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("친구 요청 목록을 불러오는 데 실패했습니다.", error);
    throw error;
  }
}

//받은 요청 목록 불러오기
export async function fetchReceivedFriendRequests(userId) {
  const token = localStorage.getItem("accessToken"); // 또는 context 등에서 가져와도 됨

  try {
    const response = await fetch(
      `/api/friend-requests/received?userId=${userId}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );

    if (!response.ok) {
      throw new Error("받은 친구 요청 불러오기 실패: " + response.status);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("에러 발생:", error);
    throw error;
  }
}
