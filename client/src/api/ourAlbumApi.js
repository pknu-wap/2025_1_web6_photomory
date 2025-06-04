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

// 우리의 추억 앨범 조회 API
export async function getOurAlbumData() {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(`${BASE_URL}/api/our-album`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (!response.ok) {
      throw new Error(`우리의 추억 조회 실패: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("❌ 우리의 추억 API 오류:", error);
    throw error;
  }
}

//우리의 추억 앨범 생성 api 함수
export async function createGroupAlbum(
  groupId,
  { albumName, albumTags, albumMakingTime, albumDescription }
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
          albumTags,
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

// 우리의 추억 앨범 상세 + 게시물 목록 조회 API
export async function fetchGroupAlbumDetail(albumId, page = 0, size = 4) {
  const token = localStorage.getItem("accessToken");

  try {
    const response = await fetch(
      `${BASE_URL}/api/our-album/album/${albumId}?page=${page}&size=${size}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );

    if (!response.ok) {
      throw new Error(`앨범 상세 조회 실패: ${response.status}`);
    }

    const data = await response.json();
    return data; // { albumId, albumName, ..., posts: [...] }
  } catch (error) {
    console.error("앨범 상세 정보 요청 중 에러:", error);
    throw error;
  }
}

// 우리의 추억 앨범 게시물 생성 api함수
export async function createGroupAlbumPost(
  albumId,
  { postTitle, postTime, photoFile }
) {
  const token = localStorage.getItem("accessToken");

  const photoName = postTitle;
  const photoMakingtime = postTime;

  const formData = new FormData();
  formData.append("photo", photoFile);

  formData.append(
    "requestDtoJson",
    JSON.stringify({
      postTitle,
      postTime,
      photoName,
      photoMakingtime,
    })
  );

  try {
    const response = await fetch(
      `${BASE_URL}/api/our-album/album/${albumId}/post`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      }
    );

    if (!response.ok) {
      throw new Error(`게시글 생성 실패: ${response.status}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("게시글 생성 중 오류:", error);
    throw error;
  }
}

//우리의 추억 게시물 삭제 api함수
export async function deleteGroupPost(albumId, postId) {
  const accessToken = localStorage.getItem("accessToken");

  try {
    const response = await fetch(
      `${BASE_URL}/api/our-album/album/${albumId}/post/${postId}`,
      {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );

    if (!response.ok) {
      throw new Error(`게시물 삭제 실패: ${response.status}`);
    }

    return response.status === 204; // 성공 시 true 반환
  } catch (error) {
    console.error("❗ 게시물 삭제 중 오류:", error);
    throw error;
  }
}

//우리의 추억 댓글 전송 api함수
export async function writeComment(albumId, postId, commentsText) {
  const token = localStorage.getItem("accessToken");
  console.log(JSON.stringify({ albumId, postId, commentsText }));
  try {
    const response = await fetch(
      `${BASE_URL}/api/our-album/${albumId}/post/${postId}/comment`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`, // 필요 없다면 제거 가능
        },
        body: JSON.stringify({ albumId, postId, commentsText }),
      }
    );

    if (!response.ok) {
      throw new Error("댓글 작성 실패: " + response.status);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error("댓글 작성 중 에러:", error);
    throw error;
  }
}

//초대 가능 친구 목록 불러오기 api함수
export async function getInvitableFriends(groupId) {
  const token = localStorage.getItem("accessToken");

  try {
    const response = await fetch(
      `${BASE_URL}/api/our-album/group/${groupId}/invitable-friends`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );

    if (!response.ok) {
      throw new Error(
        "초대 가능한 친구 목록 불러오기 실패: " + response.status
      );
    }

    const data = await response.json(); // 친구 목록 배열
    return data;
  } catch (error) {
    console.error("초대 가능한 친구 조회 중 에러:", error);
    throw error;
  }
}
