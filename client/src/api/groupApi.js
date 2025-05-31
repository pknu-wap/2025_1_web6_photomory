const BASE_URL = process.env.REACT_APP_API_URL;
//그룹 추가 api 연동함수
export async function addNewGroup({ groupName }) {
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

//그룹 리스트 조회 api 연동함수
export async function getGroupList() {
  try {
    const accessToken = localStorage.getItem("accessToken");

    const response = await fetch(`${BASE_URL}/api/ouralbum/groups`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (!response.ok) {
      throw new Error("그룹 리스트 조회 실패!");
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error(error);
    throw error;
  }
}
