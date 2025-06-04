import { useEffect, useState } from "react";
import Footer from "../component/common/Footer";
import Header from "../component/common/Header";
import MemoryNotificationBox from "../component/notification/MemoryNotificationBox";
import GeneralNotificationBox from "../component/notification/GeneralNotificationBox";
import { fetchMockMemoryNotifications } from "../api/fetchMockMemoryNotifications";
import {
  subscribeToNotifications,
  fetchnotificationList,
} from "../api/notificationApi"; // SSE 구독 함수

function NotificationPage() {
  const [memoryNotifications, setMemoryNotifications] = useState([]);
  const [generalNotifications, setGeneralNotifications] = useState([]);

  // 1. 초기 알림 목록 불러오기
  useEffect(() => {
    async function fetchData() {
      try {
        const data = await fetchnotificationList(); // 실제 알림 목록 불러오기
        const mockMemory = await fetchMockMemoryNotifications(); // 목데이터 가져오기

        // 실제 데이터 중 REMIND만 추출
        const realMemory = data.filter((item) => item.type === "REMIND");

        // 일반 알림은 type !== REMIND
        const general = data.filter((item) => item.type !== "REMIND");

        // 실제 + 목데이터 결합 (실제 데이터가 앞에 오도록)
        const mergedMemory = [...realMemory, ...mockMemory];

        setMemoryNotifications(mergedMemory);
        setGeneralNotifications(general);
      } catch (error) {
        console.error("알림 불러오기 실패:", error);
      }
    }

    fetchData();
  }, []);

  useEffect(() => {
    console.log("[SSE] 구독 useEffect 진입");

    const controller = subscribeToNotifications((type, data) => {
      console.log(`[${type}] 알림 수신됨`, data);

      // 테스트용 더미 처리 로직
      switch (type) {
        case "FRIEND_REQUEST":
          alert(`친구 요청 알림: ${data.noti_message || JSON.stringify(data)}`);
          break;
        case "REMIND":
          alert(`리마인드 알림: ${data.noti_message || JSON.stringify(data)}`);
          break;
        default:
          alert(`[${type}] 알림: ${data.noti_message || JSON.stringify(data)}`);
          break;
      }
    });

    return () => {
      console.log("[SSE] useEffect cleanup: 연결 중단");
      controller?.abort();
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
