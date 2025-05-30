import { useState } from "react";
import GeneralNotificationItem from "./GeneralNotificationItem";
import PaginationBar from "../common/PaginationBar";
import "./GeneralNotificationBox.css";

function GeneralNotificationBox({ generalNotifications }) {
  const itemsPerPage = 3;
  const [currentPage, setCurrentPage] = useState(1);

  const totalPages = Math.ceil(generalNotifications.length / itemsPerPage);
  const startIdx = (currentPage - 1) * itemsPerPage;
  const currentItems = generalNotifications.slice(
    startIdx,
    startIdx + itemsPerPage
  );

  return (
    <div className="general-box">
      <h2 className="general-title">알림</h2>

      <div className="general-list">
        {currentItems.map((item) => (
          <GeneralNotificationItem key={item.id} data={item} />
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

export default GeneralNotificationBox;
