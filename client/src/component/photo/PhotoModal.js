import React, { useState, useEffect } from "react";
import dayjs from "dayjs";
import "./PhotoModal.css";
import calenderIcon from "../../assets/calenderIcon.svg";
import modalCancelButton from "../../assets/modalCancelButton.svg";
import discardButton from "../../assets/discardButton.svg";

function PhotoModal({ type, albumId, photo, onClose, onDelete }) {
  const [animate, setAnimate] = useState(false);
  // 개인 앨범, 공유앨범 분기에 따른 사진 삭제 처리 헨들러
  const handleDeleteClick = () => {
    if (!onDelete) return;

    if (type === "private") {
      onDelete(photo.photo_id);
    } else {
      onDelete(albumId, photo.post_id, photo.photo_id, photo.photo_name);
    }

    onClose(); // 삭제 후 모달 닫기
  };

  useEffect(() => {
    if (photo) {
      const timer = setTimeout(() => setAnimate(true), 10); // DOM mount 후  10ms 후 .active 붙이기
      return () => clearTimeout(timer); //이전 타이머 제거
    } else {
      setAnimate(false); // 닫을 땐 다시 초기화
    }
  }, [photo]);

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
        className={`modalImageCard ${animate ? "active" : ""}`}
      >
        <div style={{ display: "flex", alignItems: "flex-start" }}>
          <div style={{ position: "relative" }}>
            <img
              src={photo.photo_url}
              alt={photo.photo_name}
              className="modalImage"
            />
            <img
              src={modalCancelButton}
              alt={"modalCancelButton"}
              onClick={onClose}
              style={{
                position: "absolute",
                top: "8px",
                right: "8px",
                cursor: "pointer",
              }}
            />
          </div>
          <div className="modalImageInfo">
            <div className="infoHeader">
              <h3 className="imageTitle">{photo.photo_name}</h3>
              <div className="createdDay">
                <img
                  src={calenderIcon}
                  alt="calenderIcon"
                  style={{ marginRight: "4px" }}
                />
                {/*YYYY.MM.DD*/}
                <p style={{ fontSize: "14px" }}>
                  {dayjs(photo.photo_makingtime).format("YYYY.MM.DD")}
                </p>
              </div>
            </div>
            <div className="albumTitleCard">
              <p style={{ marginBottom: "4px" }}>앨범</p>
              <p>#{photo.albumTitle}</p>
            </div>
            {/* 삭제 버튼 조건부 렌더링 */}
            {onDelete && (
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                }}
              >
                <button onClick={handleDeleteClick} className="deleteButton">
                  <img src={discardButton} alt="discardButton" />
                  삭제하기
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
}

export default PhotoModal;
