import { useState } from "react";
import MemoryNotificationCard from "./MemoryNotificationCard";
import PaginationBar from "../common/PaginationBar"; // 경로는 실제 프로젝트에 맞게
import "./MemoryNotificationBox.css";

function MemoryNotificationBox({ memoryNotifications }) {
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 6;
  console.log(memoryNotifications);
  const totalPages = Math.ceil(memoryNotifications.length / itemsPerPage);
  const startIdx = (currentPage - 1) * itemsPerPage;
  const currentItems = memoryNotifications.slice(
    startIdx,
    startIdx + itemsPerPage
  );

  return (
    <div className="memory-box">
      <h2 className="memory-title">📸 추억 회상 알림</h2>

      <div className="memory-grid">
        {currentItems.map((notification, idx) => (
          <MemoryNotificationCard key={idx} data={notification} />
        ))}
      </div>

      <PaginationBar
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
      />
    </div>
  );
}

export default MemoryNotificationBox;
