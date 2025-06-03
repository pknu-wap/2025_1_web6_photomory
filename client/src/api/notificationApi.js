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

// SSE api 연동 함수
export function subscribeToNotifications(
  onMessageCallbackByType,
  reconnectDelay = 5000
) {
  const token = localStorage.getItem("accessToken");
  if (!token) {
    console.error("❗ accessToken이 없습니다. SSE 연결 중단");
    return null;
  }

  const controller = new AbortController();
  let retryTimeout = null;

  const connect = () => {
    console.log("🔗 SSE 연결 시도 중...(with Authorization header)");

    fetchEventSource(`${BASE_URL}/api/notifications/subscribe`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`, // ✅ 헤더로 토큰 전송
      },
      signal: controller.signal,

      onopen(res) {
        if (res.ok) {
          console.log("✅ SSE 연결 성공");
        } else {
          throw new Error(`❗ 서버 응답 오류: ${res.status}`);
        }
      },

      onmessage(event) {
        const type = event.event;
        try {
          const data = JSON.parse(event.data);
          console.log(`📩 [${type}] 알림 수신`, data);
          onMessageCallbackByType(type, data);
        } catch (err) {
          console.error("❗ 데이터 파싱 오류:", err);
        }
      },

      onerror(err) {
        console.error("❗ SSE 오류:", err);
        retry();
      },
    });
  };

  const retry = () => {
    if (retryTimeout) return;
    console.log(`🔄 ${reconnectDelay / 1000}초 후 SSE 재연결 시도`);
    retryTimeout = setTimeout(connect, reconnectDelay);
  };

  controller.signal.addEventListener("abort", () => {
    if (retryTimeout) clearTimeout(retryTimeout);
  });

  connect();
  return controller;
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
