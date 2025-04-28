import React from "react";
import dayjs from "dayjs";
import PhotoModal from "./PhotoModal";
import "./PhotoGrid.css";
function PhotoGrid({ photoList, photo, onOpenModal, onClose, onDelete }) {
  const totalSlots = 8; // 4행 2열 = 8칸
  const filledPhotos = [...photoList];

  // 부족한 칸은 null로 채우기
  while (filledPhotos.length < totalSlots) {
    filledPhotos.push(null);
  }

  return (
    <div className="photoGridContainer">
      {filledPhotos.map((photo, index) => (
        <div
          key={index}
          className={`photoSlot ${photo ? "filledSlot" : "emptySlot"}`}
        >
          {photo ? (
            <>
              <img
                src={photo.photo_url}
                alt={photo.photo_name}
                className="photoImage"
                onClick={() => onOpenModal(photo)} //사진 클릭시 해당 사진으로 모달 활성화
              />
              <div className="photoInfo">
                <p className="photoTitle">{photo.photo_name}</p>
                <p className="photoDate">
                  {dayjs(photo.photo_makingtime).format("YYYY/MM/DD")}
                </p>
              </div>
            </>
          ) : (
            <span className="emptyText">빈 슬롯</span>
          )}
        </div>
      ))}
      {/*선택된 사진이 있을 시 모달 활성화 */}
      {photo && (
        <PhotoModal photo={photo} onClose={onClose} onDelete={onDelete} />
      )}
    </div>
  );
}

export default PhotoGrid;
