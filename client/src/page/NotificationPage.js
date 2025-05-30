import { useEffect, useState } from "react";
import Footer from "../component/common/Footer";
import Header from "../component/common/Header";
import MemoryNotificationBox from "../component/notification/MemoryNotificationBox";
import GeneralNotificationBox from "../component/notification/GeneralNotificationBox";
import { getnotificationList } from "../api/getNotificationList";

function NotificationPage() {
  const [memoryNotifications, setMemoryNotifications] = useState([]);
  const [generalNotifications, setGeneralNotifications] = useState([]);

  useEffect(() => {
    async function fetchData() {
      try {
        const data = await getnotificationList(); // 목데이터 배열

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

  return (
    <>
      <Header />
      <div
        style={{
          width: "1056px",
          minHeight: "600px",
          borderRadius: "8px",
          background: "#ffffff",
          margin: "64px auto",
          padding: "24px",
          boxShadow:
            "0px 4px 6px -4px rgba(0, 0, 0, 0.1),0px 10px 15px -3px rgba(0, 0, 0, 0.1)",
        }}
      >
        <MemoryNotificationBox list={memoryNotifications} />
        <GeneralNotificationBox list={generalNotifications} />
      </div>
      <Footer />
    </>
  );
}

export default NotificationPage;
