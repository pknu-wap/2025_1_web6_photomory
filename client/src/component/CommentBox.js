import React, { useState } from "react";
import sendIcon from "../assets/sendIcon.svg";
function CommentBox({ photoId }) {
  const [comment, setComment] = useState(""); //입력할 댓글
  const [comments, setComments] = useState([]); // 댓글 리스트

  const handleSubmit = (e) => {
    e.preventDefault();
    if (comment.trim() === "") return;

    const newComment = {
      text: comment, // 댓글
      date: new Date(), //현재 시간 저장
    };
    console.log(newComment.date);
    // 댓글 서버 전송
    console.log(`📸 ${photoId}번 사진 댓글:`, comment);
    setComments((prev) => [...prev, newComment]); //댓글추가
    setComment(""); // 작성 후 초기화
  };

  return (
    <div
      style={{
        width: "516px",
        height: "320px",
        backgroundColor: "#ffffff",
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between",
        padding: "12px",
        borderRadius: "8px",
        boxSizing: "border-box",
      }}
    >
      {/* 댓글 리스트 (스크롤 영역) */}
      <div
        style={{
          flex: 1,
          overflowY: "auto",
          marginBottom: "12px",
        }}
      >
        <ul style={{ paddingLeft: 0, listStyleType: "none", margin: 0 }}>
          {comments.map((c, idx) => (
            <li
              key={idx}
              style={{
                backgroundColor: "#f3f4f6",
                padding: "6px 10px",
                borderRadius: "6px",
                marginBottom: "4px",
                fontSize: "14px",
              }}
            >
              <p style={{ marginBottom: "4px" }}>사용자이름 : {c.text}</p>
              <p style={{ fontSize: "12px", color: "#888" }}>
                {c.date.toLocaleDateString("ko-KR")}
              </p>
            </li>
          ))}
        </ul>
      </div>

      {/* 댓글 입력창 */}
      <form
        onSubmit={handleSubmit}
        style={{
          display: "flex",
          alignItems: "center",
          borderWidth: "1px 0px 0px 0px",
          borderStyle: "solid",
          borderColor: "#E5E7EB",
          paddingTop: "10px",
          justifyContent: "space-between",
        }}
      >
        <input
          type="text"
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder="댓글을 입력하세요"
          style={{
            padding: "8px",
            width: "400px",
            border: "1px solid #ccc",
            borderRadius: "4px",
            marginRight: "8px",
          }}
        />
        <button
          type="submit"
          style={{
            display: "flex",
            justifyContent: "center",
            padding: "8px 12px",
            width: "78px",
            backgroundColor: "#3b82f6",
            color: "#fff",
            border: "none",
            borderRadius: "4px",
            cursor: "pointer",
          }}
        >
          <img src={sendIcon} alt="sendIcon" />
        </button>
      </form>
    </div>
  );
}

export default CommentBox;
