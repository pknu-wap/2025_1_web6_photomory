import { useEffect, useState } from "react";
import { fetchnotificationList } from "../../api/notificationApi";
import MemoryNotificationCard from "./MemoryNotificationCard";

function MemoryNotificationBox() {
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    async function fetchData() {
      try {
        const data = await fetchnotificationList();
        console.log(data);
        setNotifications(data); // 배열로 상태 저장
      } catch (error) {
        console.error("알림 불러오기 실패:", error);
      }
    }

    fetchData();
  }, []);

  return (
    <div>
      {notifications.map((notification) => (
        <MemoryNotificationCard key={notification.id} data={notification} />
      ))}
    </div>
  );
}

export default MemoryNotificationBox;
