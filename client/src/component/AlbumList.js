import React, { useState } from "react";
import { Link } from "react-router-dom";
import PaginationBar from "./PaginationBar";
import emptyImage from "../assets/emptyImage.svg";
import "./AlbumList.css";
//개인 앨범, 공유 앨범에서 각 앨범을 보여주는 컴포넌트
function AlbumList({ albums, type = "", selectedGroupId = "", basePath = "" }) {
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
        <div key={currentAlbum.album_id} className="albumCard">
          <div className="innerAlbum">
            {/* 앨범 제목 */}
            <h4 className="albumTitle">#{currentAlbum.album_name}</h4>
            <Link
              to={
                type === "group"
                  ? `${basePath}/${selectedGroupId}/${currentAlbum.album_id}`
                  : `${basePath}/${currentAlbum.album_id}`
              }
              className="albumLink"
            >
              앨범 상세보기
            </Link>
            <div className="albumInfo">
              <p>
                총 {currentAlbum.photos.length}장의 사진들 | 생성일:{" "}
                {currentAlbum.album_makingtime}
              </p>
            </div>
          </div>
          {/* 사진미리보기(최대4개) */}
          <div className="albums">
            {/* 사진 4개까지 미리보기 */}
            {currentAlbum.photos.slice(0, 4).map((photo) => (
              <div
                key={photo.photo_id}
                style={{
                  textAlign: "center",
                }}
              >
                <img
                  src={photo.photo_url}
                  alt={photo.photo_name}
                  className="photo"
                />
              </div>
            ))}
            {/* 부족한 개수만큼 placeholder 추가 */}
            {Array.from({ length: 4 - currentAlbum.photos.length }).map(
              (_, idx) => (
                <div key={`placeholder-${idx}`} className="emptyCard">
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
