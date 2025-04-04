import React from "react";

function PhotoModal({ photo, onClose }) {
  if (!photo) return null;

  return (
    <>
      {/* 어두운 배경 */}
      <div
        onClick={onClose} //클릭시 닫힘
        style={{
          position: "fixed",
          top: 0,
          left: 0,
          width: "100vw",
          height: "100vh",
          background: "rgba(0,0,0,0.4)",
          zIndex: 1000,
        }}
      />

      {/* 모달 본체 */}
      <div
        onClick={(e) => e.stopPropagation()} // 이벤츠 전파 막기
        style={{
          position: "fixed",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          backgroundColor: "#fff",
          padding: "20px",
          borderRadius: "8px",
          zIndex: 1001,
          boxShadow: "0 0 10px rgba(0,0,0,0.3)",
          textAlign: "center",
        }}
      >
        <h4>#{photo.albumTitle}</h4>
        <img
          src={photo.imgUrl}
          alt={photo.title}
          style={{ width: "300px", borderRadius: "8px" }}
        />
        <p style={{ marginTop: "10px" }}>{photo.title}</p>
        <button onClick={onClose}>닫기</button>
      </div>
    </>
  );
}

export default PhotoModal;
