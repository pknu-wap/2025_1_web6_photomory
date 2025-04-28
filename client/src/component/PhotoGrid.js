import React from "react";
import dayjs from "dayjs";

function PhotoGrid({ photoList }) {
  const totalSlots = 8; // 4행 2열 = 8칸
  const filledPhotos = [...photoList];

  // 부족한 칸은 null로 채우기
  while (filledPhotos.length < totalSlots) {
    filledPhotos.push(null);
  }

  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: "repeat(2, 1fr)",
        gridTemplateRows: "repeat(4, 1fr)",
        gap: "24px",
        width: "100%",
        maxWidth: "1088px",
        margin: "0 auto",
      }}
    >
      {filledPhotos.map((photo, index) => (
        <div
          key={index}
          style={{
            width: "100%",
            height: "350px",
            borderRadius: "8px",
            overflow: "hidden",
            background: photo ? "#ffffff" : "#f9f9f9",
            border: photo ? "1px solid #ccc" : "2px dashed #bbb",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            textAlign: "center",
          }}
        >
          {photo ? (
            <>
              <img
                src={photo.photo_url}
                alt={photo.photo_name}
                style={{
                  width: "100%",
                  height: "300px",
                  objectFit: "cover",
                }}
              />
              <div style={{ padding: "8px" }}>
                <p
                  style={{
                    fontWeight: "bold",
                    fontSize: "14px",
                    marginBottom: "4px",
                  }}
                >
                  {photo.photo_name}
                </p>
                <p style={{ color: "#6B7280", fontSize: "12px" }}>
                  {dayjs(photo.photo_makingtime).format("YYYY/MM/DD")}
                </p>
              </div>
            </>
          ) : (
            <span style={{ color: "#bbb", fontSize: "14px" }}>빈 슬롯</span>
          )}
        </div>
      ))}
    </div>
  );
}

export default PhotoGrid;
