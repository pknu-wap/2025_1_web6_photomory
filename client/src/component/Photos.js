import { useState } from "react";
import CommentBox from "./CommentBox";
import dayjs from "dayjs";
import PaginationBar from "./PaginationBar";

function Photos({ albumTitle, album }) {
  const [currentPage, setCurrentPage] = useState(1); //현재 페이지 상태
  const photosPerPage = 4; //한 페이지당  사진 갯수

  // 전체 페이지 수 계산
  const totalPages = Math.ceil(album.photos.length / photosPerPage);

  // 현재 페이지의 사진진들
  const indexOfLastPhoto = currentPage * photosPerPage; //마지막앨범
  const indexOfFirstPhoto = indexOfLastPhoto - photosPerPage; //첫번째앨범
  const currentPhotos = album.photos.slice(indexOfFirstPhoto, indexOfLastPhoto); //앨범 범위

  // 페이지 이동 핸들러
  const handlePageClick = (page) => {
    setCurrentPage(page);
  };
  return (
    <div
      style={{
        marginBottom: "30px",
      }}
    >
      <div style={{ height: "128px" }}>
        <h2>#{album.title}</h2>
        <p>현재 보고 계신 앨범은 "{albumTitle}"태그의 사진들입니다.</p>
      </div>

      {currentPhotos.map((photo) => (
        <div
          key={photo.id}
          style={{ display: "flex", gap: "24px", marginBottom: "24px" }}
        >
          <img
            src={photo.imgUrl}
            alt={photo.title}
            style={{ borderRadius: "8px", width: "516px", height: "400px" }}
          />
          <div>
            <h3>{photo.title}</h3>
            <p>{dayjs(photo.createdAt).format("YYYY/MM/DD")}</p>
            <CommentBox />
          </div>
        </div>
      ))}
      <PaginationBar
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={handlePageClick}
      />
    </div>
  );
}

export default Photos;
