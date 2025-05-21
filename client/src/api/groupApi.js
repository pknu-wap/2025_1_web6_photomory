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
    console.error(error);
    throw error;
  }
}

export async function getGroupList() {
  try {
    const accessToken = localStorage.getItem("accessToken");

    const response = await fetch(`${BASE_URL}/api/ouralbum/groups`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (!response.ok) {
      throw new error("그룹 리스트 조회 실패!");
    }
  } catch (error) {
    console.error("그룹 리스트 :", error);
  }
}
