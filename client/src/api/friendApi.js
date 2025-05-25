const BASE_URL = process.env.REACT_APP_API_URL;

//친구 이름 검색 api
export async function searchFriendByName(name) {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(
      `${BASE_URL}/api/friends/search?name=${encodeURIComponent(name)}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );

    if (!response.ok) {
      throw new Error(`친구 검색 실패: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("친구 검색 중 오류:", error);
    throw error;
  }
}

// 친구 요청 보내기 api 함수
export async function sendFriendRequest(senderId, receiverId) {
  const accessToken = localStorage.getItem("accessToken");

  const formBody = new URLSearchParams();
  formBody.append("senderId", senderId);
  formBody.append("receiverId", receiverId);
  console.log("요청 바디:", formBody.toString());

  try {
    const response = await fetch(`${BASE_URL}/api/friend-requests/send`, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        Authorization: `Bearer ${accessToken}`,
      },
      body: formBody.toString(),
    });

    if (!response.ok) {
      throw new Error(`친구 요청 실패: ${response.status}`);
    }

    const result = await response.json(); // 또는 .text() 서버 응답 형식에 따라 조정
    return result;
  } catch (error) {
    console.error("친구 요청 중 오류:", error);
    throw error;
  }
}
