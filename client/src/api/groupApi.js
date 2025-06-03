const BASE_URL = process.env.REACT_APP_API_URL;

export async function inviteFriendToGroup(groupId, userId) {
  const accessToken = localStorage.getItem("accessToken");

  // 하나의 userId를 객체 배열로 감쌈
  const requestBody = [{ user_id: userId }];

  try {
    const response = await fetch(
      `${BASE_URL}/api/our-album/group/${groupId}/invite`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify(requestBody),
      }
    );

    if (!response.ok) {
      throw new Error(`친구 초대 실패: ${response.status}`);
    }

    const result = await response.text(); // "친구 초대가 완료되었습니다."
    return result;
  } catch (error) {
    console.error("친구 초대 중 오류:", error);
    throw error;
  }
}

export async function removeFriendFromGroup(groupId, userIdToRemove) {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(
      `${BASE_URL}/api/our-album/group/${groupId}/member/${userIdToRemove}`,
      {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );

    return response.status === 204;
  } catch (error) {
    console.error("친구 삭제 중 오류:", error);
    throw error;
  }
}
