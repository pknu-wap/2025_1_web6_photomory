import React, { useState } from "react";
import PaginationBar from "./PaginationBar";

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
            border: "1px solid #ddd",
            borderRadius: "8px",
            padding: "16px",
            marginBottom: "24px",
            backgroundColor: "#fefefe",
          }}
        >
          <div style={{ display: "flex" }}>
            {/* 앨범 제목 */}
            <h4>#{currentAlbum.title}</h4>
            <p>총 {currentAlbum.photos.length}장의 사진들</p>
            <p
              style={{
                color: "#1a73e8",
                textDecoration: "underline",
                cursor: "pointer",
              }}
            >
              앨범 상세보기 (준비 중)
            </p>
            <p style={{ color: "#666", fontSize: "14px" }}>
              생성일: {currentAlbum.createdAt}
            </p>
          </div>
          {/* 사진미리보기(최대4개) */}
          <div
            style={{
              display: "flex",
              flexWrap: "wrap",
              gap: "12px",
              marginTop: "12px",
            }}
          >
            {/* 사진 4개까지 미리보기 */}
            {currentAlbum.photos.slice(0, 4).map((photo) => (
              <div
                key={photo.id}
                style={{
                  width: "150px",
                  textAlign: "center",
                }}
              >
                <img
                  src={photo.imgUrl}
                  alt={photo.title}
                  style={{
                    width: "100%",
                    height: "100px",
                    objectFit: "cover",
                    borderRadius: "4px",
                    marginBottom: "4px",
                  }}
                />
                <div style={{ fontSize: "14px" }}>{photo.title}</div>
              </div>
            ))}
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
