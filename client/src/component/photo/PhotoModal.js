import React from "react";
import dayjs from "dayjs";
import "./PhotoModal.css";
import calenderIcon from "../../assets/calenderIcon.svg";
import modalCancelButton from "../../assets/modalCancelButton.svg";
import discardButton from "../../assets/discardButton.svg";

function PhotoModal({ photo, onClose, onDelete }) {
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
                <button
                  onClick={() => {
                    onDelete(photo.photo_id);
                    onClose();
                  }}
                  className="deleteButton"
                >
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
