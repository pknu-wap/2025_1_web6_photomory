import { fetchEventSource } from "@microsoft/fetch-event-source";
const BASE_URL = process.env.REACT_APP_API_URL;

//친구 요청 목록 불러오기 api함수
export async function sentFriendRequests(userId) {
  const token = localStorage.getItem("accessToken"); // 또는 쿠키 등에서 가져옴

  try {
    const response = await fetch(
      `${BASE_URL}/api/friend-requests/sent?userId=${userId}`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      }
    );

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

//받은 요청 목록 불러오기 api함수
export async function getReceivedFriendRequests(userId) {
  const token = localStorage.getItem("accessToken");

  try {
    const response = await fetch(
      `${BASE_URL}/api/friend-requests/received?userId=${userId}`,
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

export function subscribeToNotifications(onMessageCallback) {
  const token = localStorage.getItem("accessToken"); // 또는 'token'

  if (!token) {
    console.error("❗ accessToken이 없습니다. SSE 연결 중단");
    return;
  }

  fetchEventSource(`${BASE_URL}/api/notifications/subscribe`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },

    onopen(response) {
      console.log("✅ SSE 연결 성공:", response.status);
    },

    onmessage(event) {
      console.log("📩 서버로부터 알림:", event.data);
      try {
        const data = JSON.parse(event.data);
        onMessageCallback(data); // 콜백에 알림 객체 전달
      } catch (err) {
        console.error("❗ 알림 파싱 오류:", err);
      }
    },

    onclose() {
      console.log("🔌 SSE 연결 종료");
    },

    onerror(err) {
      console.error("❗ SSE 연결 오류 발생:", err);
      // fetch-event-source는 자동으로 재시도합니다 (AbortController로 끊을 수 있음)
    },
  });
}

//알림 목록 조회 api함수
export async function fetchnotificationList() {
  const token = localStorage.getItem("accessToken"); // 또는 context 등에서

  try {
    const response = await fetch(`${BASE_URL}/api/notifications/list-read`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });

    if (!response.ok) {
      throw new Error("알림 목록 불러오기 실패: " + response.status);
    }

    const data = await response.json();
    return data; // 알림 목록 배열
  } catch (error) {
    console.error("알림 목록 요청 중 에러:", error);
    throw error;
  }
}

//알림 전송 api함수
export async function sendNotification({ receiverId, message, type }) {
  const token = localStorage.getItem("accessToken");

  try {
    const response = await fetch(`${BASE_URL}/api/notifications/send`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({
        receiverId,
        message,
        type,
      }),
    });

    if (!response.ok) {
      throw new Error("알림 전송 실패: " + response.status);
    }

    const result = await response.json(); // 성공 응답
    return result;
  } catch (error) {
    console.error("알림 전송 중 에러 발생:", error);
    throw error;
  }
}
