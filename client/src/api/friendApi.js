const BASE_URL = process.env.REACT_APP_API_URL;

// 친구가 아닌 사용자 검색 api
export async function searchNonFriendByKeyword(keyword) {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(
      `${BASE_URL}/api/friend-requests/non-friends/search?keyword=${encodeURIComponent(
        keyword
      )}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );

    if (!response.ok) {
      throw new Error(`비친구 검색 실패: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("비친구 검색 중 오류:", error);
    throw error;
  }
}

//친구요청보내기 api 함수
export async function sendFriendRequest(receiverId) {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(
      `${BASE_URL}/api/friend-requests/send?receiverId=${receiverId}`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );

    if (!response.ok) {
      throw new Error(`친구 요청 실패: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("친구 요청 중 오류:", error);
    throw error;
  }
}

//친구 요청 수락 api 함수
export async function acceptFriendRequest(requestId) {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(
      `${BASE_URL}/api/friend-requests/${requestId}/accept`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "application/json",
        },
      }
    );

    if (!response.ok) {
      throw new Error(`친구 요청 수락 실패: ${response.status}`);
    }

    return true;
  } catch (error) {
    console.error("친구 요청 수락 중 오류 발생:", error);
    throw error;
  }
}

//친구 요청 거절 api 함수
export async function rejectFriendRequest(requestId) {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(`${BASE_URL}/api/friend-list/${requestId}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error(`친구 요청 거절 실패: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("친구 요청 거절 중 오류:", error);
    throw error;
  }
}
