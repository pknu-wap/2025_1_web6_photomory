const BASE_URL = process.env.REACT_APP_API_URL;

export async function addNewGroup({ groupName, groupDescription }) {
  try {
    const accessToken = localStorage.getItem("accessToken");

    const response = await fetch(`${BASE_URL}/api/ouralbum/groups`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify({
        groupName,
        groupDescription,
      }),
    });

    if (!response.ok) {
      throw new Error("그룹 생성 실패!");
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("그룹 생성 중 오류 발생:", error);
    throw error;
  }
}
