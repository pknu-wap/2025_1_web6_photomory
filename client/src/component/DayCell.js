import React, { useState } from "react";
import "./Calender"; //Calender 컴포넌트 css사용
import PhotoModal from "./PhotoModal";

//달력 안에 들어가 있는 날짜 컴포넌트
function DayCell({
  day,
  isEmpty = false,
  photos = [],
  albumColorsMap = {},
  albumDotColorsMap = {},
}) {
  //day: 날짜, isEmpty:빈 칸인지 여부 (true이면 날짜 없음)
  const [selectedPhoto, setSelectedPhoto] = useState(null);

  const handlePhotoClick = (photo) => {
    //클릭한 사진 정보 변경 헨들러
    setSelectedPhoto(photo);
  };

  const handleCloseModal = () => {
    //모달 닫기 헨들러
    setSelectedPhoto(null);
  };

  // 해당 날짜에 사진이 있다면 첫 번째 사진의 앨범 색상 사용
  const firstPhoto = photos[0];
  const bgColor =
    firstPhoto && albumColorsMap[firstPhoto.albumTitle] //앨범명에 매핑된 배경색
      ? albumColorsMap[firstPhoto.albumTitle]
      : "#fff"; // 기본 흰색

  return (
    <div
      className={`day ${isEmpty ? "empty" : ""}`}
      style={{
        backgroundColor: isEmpty ? "transparent" : bgColor, // 빈칸은 배경 없음
        borderRadius: "8px",
        padding: "8px",
        minHeight: "80px",
        boxSizing: "border-box",
      }}
    >
      {!isEmpty && (
        <>
          {/* 날짜 숫자 */}
          <strong>{day}</strong>

          {/* 앨범명: 사진명 목록 */}
          <div
            style={{
              display: "flex",
              gap: "4px",
              marginBottom: "6px",
              flexWrap: "wrap",
              justifyContent: "center",
              cursor: "pointer",
            }}
          >
            {photos.map((photo, idx) => {
              const dotColor = albumDotColorsMap[photo.albumTitle] || "#333"; //매핑된 점 색깔
              return (
                <div
                  key={idx}
                  style={{
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    gap: "4px",
                    fontSize: "12px",
                    color: "#333",
                  }}
                  onClick={() => handlePhotoClick(photo)} //클릭 시 selectedPhoto 설정
                >
                  <span
                    style={{
                      cursor: "pointer",
                      width: "10px",
                      height: "10px",
                      backgroundColor: dotColor,
                      borderRadius: "50%",
                      display: "inline-block",
                    }}
                  />
                  <span>
                    <strong>#{photo.albumTitle}</strong>:<br />
                    {photo.title}
                  </span>
                  {/* 모달 표시 */}
                  <PhotoModal
                    photo={selectedPhoto}
                    onClose={handleCloseModal}
                  />
                </div>
              );
            })}
          </div>
        </>
      )}
    </div>
  );
}

export default DayCell;
