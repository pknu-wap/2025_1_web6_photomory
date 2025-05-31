import React from "react";
import { sendFriendRequest } from "../api/friendApi"; // 실제 경로로 수정

function FriendRequestTestPage() {
  const receiverId = 1;

  const handleRequest = async () => {
    try {
      const result = await sendFriendRequest(receiverId);
      console.log("✅ 친구 요청 성공:", result);
      alert(`요청 완료! 요청 ID: ${result.requestId}`);
    } catch (err) {
      console.error("❌ 친구 요청 실패:", err);
      alert("친구 요청 실패, 콘솔을 확인하세요.");
    }
  };

  return (
    <div style={{ padding: "40px" }}>
      <h2>👥 친구 요청 테스트</h2>
      <p>
        receiverId: <strong>{receiverId}</strong>
      </p>
      <button onClick={handleRequest}>친구 요청 보내기</button>
    </div>
  );
}

export default FriendRequestTestPage;
