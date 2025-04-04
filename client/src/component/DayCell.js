import "./Calender"; //Calender 컴포넌트 css사용

//달력 안에 들어가 있는 날짜 컴포넌트
function DayCell({ day, isEmpty = false, photos = [], albumColorsMap = {} }) {
  //day: 날짜, isEmpty:빈 칸인지 여부 (true이면 날짜 없음)

  // 해당 날짜에 사진이 있다면 첫 번째 사진의 앨범 색상 사용
  const firstPhoto = photos[0];
  const bgColor =
    firstPhoto && albumColorsMap[firstPhoto.albumTitle]
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
          <strong>{day}</strong>

          {/* 앨범명: 사진명 목록 */}
          <div style={{ marginTop: "6px" }}>
            {photos.map((photo, idx) => (
              <div key={idx} style={{ fontSize: "12px", color: "#333" }}>
                <strong>#{photo.albumTitle}</strong>: {photo.title}
              </div>
            ))}
          </div>
        </>
      )}
    </div>
  );
}

export default DayCell;
