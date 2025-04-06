import React from "react";
import "./PhotoModal.css";
function PhotoModal({ photo, onClose }) {
  if (!photo) return null;

  return (
    <>
      {/* 어두운 배경 */}
      <div
        onClick={onClose} //클릭시 닫힘
        className="photoModalBackColor"
      />

      {/* 모달 본체 */}
      <div
        onClick={(e) => e.stopPropagation()} // 이벤츠 전파 막기
        className="modalImageCard"
      >
        <div style={{ display: "flex" }}>
          <img
            src={photo.imgUrl}
            alt={photo.title}
            style={{ width: "864px", height: "658px", borderRadius: "8px" }}
          />
          <h3 style={{ marginTop: "10px" }}>{photo.title}</h3>
          <h4>#{photo.albumTitle}</h4>
          <button onClick={onClose}>닫기</button>
        </div>
      </div>
    </>
  );
}

export default PhotoModal;
