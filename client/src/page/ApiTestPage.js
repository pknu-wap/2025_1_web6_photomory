import React, { useState } from "react";
import { writeComment } from "../api/ourAlbumApi"; // 실제 파일 경로에 맞게 수정하세요

function CommentTestPage() {
  const [comment, setComment] = useState("");
  const albumId = 3;
  const postId = 2;
  const handleSubmit = async () => {
    if (!comment.trim()) {
      alert("댓글을 입력하세요.");
      return;
    }

    try {
      const result = await writeComment(
        albumId,
        postId,
        comment // ✅ 서버가 기대하는 구조
      );

      console.log("✅ 댓글 작성 성공:", result);
      alert("댓글 작성 완료!");
      setComment("");
    } catch (err) {
      console.error("❌ 댓글 작성 실패:", err);
      alert("댓글 작성 실패, 콘솔 확인");
    }
  };

  return (
    <div style={{ padding: "40px" }}>
      <h2>💬 댓글 작성 테스트</h2>
      <textarea
        value={comment}
        onChange={(e) => setComment(e.target.value)}
        placeholder="댓글을 입력하세요"
        rows={4}
        cols={50}
      />
      <br />
      <button onClick={handleSubmit}>댓글 전송</button>
    </div>
  );
}

export default CommentTestPage;
