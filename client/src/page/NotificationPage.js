import { useEffect, useState } from "react";
import Footer from "../component/common/Footer";
import Header from "../component/common/Header";
import MemoryNotificationBox from "../component/notification/MemoryNotificationBox";
import GeneralNotificationBox from "../component/notification/GeneralNotificationBox";
import { getnotificationList } from "../api/getNotificationList";
import { subscribeToNotifications } from "../api/notificationApi"; // SSE 구독 함수

function NotificationPage() {
  const [memoryNotifications, setMemoryNotifications] = useState([]);
  const [generalNotifications, setGeneralNotifications] = useState([]);

  // 1. 초기 알림 목록 불러오기
  useEffect(() => {
    async function fetchData() {
      try {
        const data = await getnotificationList(); // 목데이터
        const memory = data.filter((item) => item.type === "REMIND");
        const general = data.filter((item) => item.type !== "REMIND");
        setMemoryNotifications(memory);
        setGeneralNotifications(general);
      } catch (error) {
        console.error("알림 불러오기 실패:", error);
      }
    }

    fetchData();
  }, []);

  // 2. SSE 구독 - 알림 오면 콘솔에 출력
  useEffect(() => {
    const unsubscribe = subscribeToNotifications((data) => {
      console.log("🚀 실시간 알림 수신:", data);
    });

    // 수동 종료용: fetch-event-source 사용 시 AbortController 설정 가능
    return () => {
      if (unsubscribe && typeof unsubscribe.abort === "function") {
        unsubscribe.abort(); // optional
      }
    };
  }, []);

  return (
    <>
      <Header />
      <div
        style={{
          width: "1056px",
          height: "1068px",
          borderRadius: "8px",
          background: "#ffffff",
          margin: "64px auto",
          padding: "24px",
          boxShadow:
            "0px 4px 6px -4px rgba(0, 0, 0, 0.1),0px 10px 15px -3px rgba(0, 0, 0, 0.1)",
        }}
      >
        <MemoryNotificationBox memoryNotifications={memoryNotifications} />
        <GeneralNotificationBox generalNotifications={generalNotifications} />
      </div>
      <Footer />
    </>
  );
}

export default NotificationPage;
