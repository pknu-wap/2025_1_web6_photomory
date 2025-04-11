import React, { useState } from "react";

function CommentBox({ photoId }) {
  const [comment, setComment] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    if (comment.trim() === "") return;

    // 댓글 서버 전송
    console.log(`📸 ${photoId}번 사진 댓글:`, comment);

    setComment(""); // 작성 후 초기화
  };

  return (
    <form onSubmit={handleSubmit} style={{ marginTop: "8px" }}>
      <input
        type="text"
        value={comment}
        onChange={(e) => setComment(e.target.value)}
        placeholder="댓글을 입력하세요"
        style={{
          padding: "8px",
          width: "240px",
          border: "1px solid #ccc",
          borderRadius: "4px",
          marginRight: "8px",
        }}
      />
      <button
        type="submit"
        style={{
          padding: "8px 12px",
          backgroundColor: "#3b82f6",
          color: "#fff",
          border: "none",
          borderRadius: "4px",
          cursor: "pointer",
        }}
      >
        작성
      </button>
    </form>
  );
}

export default CommentBox;
