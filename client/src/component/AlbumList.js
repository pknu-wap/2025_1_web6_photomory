import React, { useState } from "react";
import PaginationBar from "./PaginationBar";
import emptyImage from "../assets/emptyImage.svg";

function AlbumList({ albums }) {
  const [currentPage, setCurrentPage] = useState(1); //현재 페이지 상태
  const albumsPerPage = 4; //한 페이지당 앨범 갯수

  // 전체 페이지 수 계산
  const totalPages = Math.ceil(albums.length / albumsPerPage);

  // 현재 페이지의 앨범들
  const indexOfLastAlbum = currentPage * albumsPerPage; //마지막앨범
  const indexOfFirstAlbum = indexOfLastAlbum - albumsPerPage; //첫번째앨범
  const currentAlbums = albums.slice(indexOfFirstAlbum, indexOfLastAlbum); //앨범 범위

  // 페이지 이동 핸들러
  const handlePageClick = (page) => {
    setCurrentPage(page);
  };

  if (!albums || albums.length === 0) {
    return <p>앨범이 없습니다.</p>;
  }

  return (
    <div>
      {currentAlbums.map((currentAlbum) => (
        <div
          key={currentAlbum.id}
          style={{
            width: "1088px",
            height: "297px",
            border: "1px solid #ddd",
            borderRadius: "8px",
            padding: "24px",
            marginBottom: "24px",
            backgroundColor: "#fefefe",
            boxShadow:
              "0px 2px 4px -2px rgba(0, 0, 0, 0.1),0px 4px 6px -1px rgba(0, 0, 0, 0.1)",
          }}
        >
          <div
            style={{ display: "flex", width: "1040px", marginBottom: "23px" }}
          >
            {/* 앨범 제목 */}
            <h4
              style={{
                marginRight: "16px",
                width: "180px",
                height: "36px",
                borderRadius: "9999px",
                background: "#000000",
                color: "#ffffff",
                display: "flex", // 가로·세로 정렬을 위해 flex 사용
                justifyContent: "center", // 가로 가운데
                alignItems: "center", // 세로 가운데
              }}
            >
              #{currentAlbum.title}
            </h4>
            <p
              style={{
                width: "112px",
                height: "28px",
                borderRadius: "9999px",
                backgroundColor: "#000000",
                color: "#ffffff",
                textAlign: "center",
                textDecoration: "none",
                cursor: "pointer",
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                alignSelf: "flex-end", //아래 라인 맞춤
                fontSize: "14px",
                marginRight: "16px",
              }}
            >
              앨범 상세보기
            </p>
            <div
              style={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                gap: "16px", // 글 사이 간격
                color: "#666",
                fontSize: "14px",
              }}
            >
              <p>
                총 {currentAlbum.photos.length}장의 사진들 | 생성일:{" "}
                {currentAlbum.createdAt}
              </p>
            </div>
          </div>
          {/* 사진미리보기(최대4개) */}
          <div
            style={{
              width: "1040px",
              height: "192px",
              display: "flex",
              flexWrap: "wrap",
              gap: "16px",
              marginTop: "12px",
            }}
          >
            {/* 사진 4개까지 미리보기 */}
            {currentAlbum.photos.slice(0, 4).map((photo) => (
              <div
                key={photo.id}
                style={{
                  textAlign: "center",
                }}
              >
                <img
                  src={photo.imgUrl}
                  alt={photo.title}
                  style={{
                    width: "248px",
                    height: "192px",
                    objectFit: "cover",
                    borderRadius: "4px",
                    marginBottom: "4px",
                  }}
                />
              </div>
            ))}
            {/* 부족한 개수만큼 placeholder 추가 */}
            {Array.from({ length: 4 - currentAlbum.photos.length }).map(
              (_, idx) => (
                <div
                  key={`placeholder-${idx}`}
                  style={{
                    width: "248px",
                    height: "192px",
                    borderRadius: "4px",
                    backgroundColor: "#f3f4f6",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                >
                  <img src={emptyImage} alt="emptyImage" />
                </div>
              )
            )}
          </div>
        </div>
      ))}
      {/*페이지네이션 바 */}
      <PaginationBar
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={handlePageClick}
      />
    </div>
  );
}

export default AlbumList;
