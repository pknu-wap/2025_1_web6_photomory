import React, { useState } from "react";
import sendIcon from "../../assets/sendIcon.svg";
import { useAuth } from "../../contexts/AuthContext";
import { writeComment } from "../../api/ourAlbumApi";
import "./CommentBox.css";
function CommentBox({ initialComments, albumId, postId }) {
  const [comment, setComment] = useState(""); //입력할 댓글
  const [comments, setComments] = useState(initialComments ?? []); //받아온 댓글들로 초기화
  const { name } = useAuth(); // 로그인한 사용자 이름 가져오기

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (comment.trim() === "") return;

    const newComment = {
      comment_text: comment, // 댓글
      created_at: new Date(), //현재 시간 저장
      user_name: name,
    };

    try {
      await writeComment(albumId, postId, comment);
      setComments((prev) => [...prev, newComment]); // 성공 후 추가
      setComment(""); // 작성 후 초기화
    } catch (err) {
      console.error("❗ 댓글 작성 실패:", err);
      alert("댓글 작성에 실패했습니다.");
    }
  };

  return (
    <div className="commentBox">
      {/* 댓글 리스트 (스크롤 영역) */}
      <div className="commentList">
        <ul style={{ paddingLeft: 0, listStyleType: "none", margin: 0 }}>
          {comments.map((c) => (
            <li key={c.comment_id} className="commentItem">
              <p style={{ marginBottom: "4px" }}>
                {c.user_name} : {c.comment_text}
              </p>
              <p className="commentDate">
                {new Date(c.created_at).toLocaleDateString("ko-KR")}
              </p>
            </li>
          ))}
        </ul>
      </div>

      {/* 댓글 입력창 */}
      <form onSubmit={handleSubmit} className="commentForm">
        <input
          type="text"
          value={comment}
          onChange={(e) => setComment(e.target.value)}
          placeholder="댓글을 입력하세요"
          className="commentInput"
        />
        <button type="submit" className="sendCommentButton">
          <img src={sendIcon} alt="sendIcon" />
        </button>
      </form>
    </div>
  );
}

export default CommentBox;
