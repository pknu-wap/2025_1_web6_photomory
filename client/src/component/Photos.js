import { useState } from "react";
import CommentBox from "./CommentBox";
import dayjs from "dayjs";
import PhotoModal from "./PhotoModal";
import PaginationBar from "./PaginationBar";

function Photos({ albumTitle, photoList, onDeltePhoto }) {
  const [selectedPhoto, setSelectedPhoto] = useState(null); //선택된 이미지 상태
  const [currentPage, setCurrentPage] = useState(1); //현재 페이지 상태
  const photosPerPage = 4; //한 페이지당  사진 갯수

  //모달 오픈 헨들러
  const handleOpenModal = (photo) => setSelectedPhoto({ ...photo, albumTitle }); //객체의 형태로 앨범명 추가
  //모달 닫기 헨들러
  const handleCloseModal = () => setSelectedPhoto(null);

  // 전체 페이지 수 계산
  const totalPages = Math.ceil(photoList.length / photosPerPage);

  // 현재 페이지의 사진진들
  const indexOfLastPhoto = currentPage * photosPerPage; //마지막앨범
  const indexOfFirstPhoto = indexOfLastPhoto - photosPerPage; //첫번째앨범
  const currentPhotos = photoList.slice(indexOfFirstPhoto, indexOfLastPhoto); //앨범 범위

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
        <h2>#{albumTitle}</h2>
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
            style={{
              borderRadius: "8px",
              width: "516px",
              height: "400px",
              cursor: "pointer",
            }}
            onClick={() => handleOpenModal(photo)}
          />
          <div>
            <h3>{photo.title}</h3>
            <p>{dayjs(photo.createdAt).format("YYYY/MM/DD")}</p>
            <CommentBox />
          </div>
          <PhotoModal
            photo={selectedPhoto}
            onClose={handleCloseModal}
            onDelete={onDeltePhoto}
          />
        </div>
      ))}
      <PaginationBar
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={handlePageClick}
        onDelte={onDeltePhoto} //상위 컴포넌트의 사진 삭제 컨트롤러
      />
    </div>
  );
}

export default Photos;
