import { useState } from "react";
import CommentBox from "../common/CommentBox";
import dayjs from "dayjs";
import PhotoModal from "./PhotoModal";
import DirectionPagination from "../common/DirectionPagination";
import PaginationBar from "../common/PaginationBar";
import privateIcon from "../../assets/privateIcon.svg";
import PhotoGrid from "./PhotoGrid";
import "./Photos.css";
function Photos({
  type,
  albumId,
  albumTitle,
  photoList = [],
  onDeltePhoto,
  currentPage,
  setCurrentPage,
  isLastPage,
}) {
  const [selectedPhoto, setSelectedPhoto] = useState(null); //선택된 이미지 상태

  const photosPerPage = 8; //private일때 한 페이지 당 8개의 사진
  const totalPages =
    type === "private" ? Math.ceil(photoList.length / photosPerPage) : 1;
  // 현재 페이지의 사진들
  let currentPhotos;

  if (type === "private") {
    const indexOfLastPhoto = currentPage * photosPerPage;
    const indexOfFirstPhoto = indexOfLastPhoto - photosPerPage;
    currentPhotos = photoList.slice(indexOfFirstPhoto, indexOfLastPhoto);
  } else {
    // 그룹 앨범일 경우 서버에서 페이지별로 받아온 4개 사진을 그대로 사용
    currentPhotos = photoList;
  }
  //모달 오픈 헨들러
  const handleOpenModal = (photo) => setSelectedPhoto({ ...photo, albumTitle }); //객체의 형태로 앨범명 추가
  //모달 닫기 헨들러
  const handleCloseModal = () => setSelectedPhoto(null);

  // 페이지 이동 핸들러
  const handlePageClick = (page) => {
    setCurrentPage(page);
  };

  return (
    <div className="photosContainer">
      {/* private 타입일 때만  아이콘 보여주기 */}
      <div className="privateHeader">
        <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
          {type === "private" && (
            <img src={privateIcon} alt="privateIcon" className="privateIcon" />
          )}
          <h2 className="albumTitleByPrivate">#{albumTitle}</h2>
        </div>
        <p>현재 보고 계신 앨범은 "{albumTitle}"태그의 사진들입니다.</p>
      </div>

      {/* type에 따라 다르게 사진 렌더링 */}
      {currentPhotos.length === 0 ? (
        <div className="noPhotosCard">
          <p>📭 아직 등록된 사진이 없습니다.</p>
          <p>지금 첫 번째 추억을 추가해보세요!</p>
        </div>
      ) : type === "private" ? (
        <PhotoGrid
          photoList={currentPhotos}
          photo={selectedPhoto}
          onOpenModal={handleOpenModal}
          onClose={handleCloseModal}
          onDelete={onDeltePhoto}
        />
      ) : (
        currentPhotos.map((photo) => (
          <div key={photo.photo_id} className="photoItem">
            <img
              src={photo.photo_url}
              alt={photo.photo_name}
              className="photoImageByGroup"
              onClick={() => handleOpenModal(photo)}
            />
            <div>
              <h3 className="photoTitle">{photo.photo_name}</h3>
              <p className="photoDate">
                {dayjs(photo.photo_makingtime).format("YYYY/MM/DD")}
              </p>
              {/* group 타입일 때만 댓글 입력창 */}
              {type === "group" && (
                <CommentBox
                  initialComments={photo.comments}
                  albumId={albumId}
                  postId={photo.post_id}
                />
              )}
            </div>
          </div>
        ))
      )}

      {/* 모달 */}
      <PhotoModal
        type={type}
        albumId={albumId} //사진 삭제시 사용되는 albumId
        photo={selectedPhoto}
        onClose={handleCloseModal}
        onDelete={onDeltePhoto}
      />

      {type === "private" ? (
        <PaginationBar
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={handlePageClick}
        />
      ) : (
        <DirectionPagination
          currentPage={currentPage}
          onPrev={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
          onNext={() => setCurrentPage((prev) => prev + 1)}
          isFirstPage={currentPage === 1}
          isLastPage={isLastPage}
        />
      )}
    </div>
  );
}

export default Photos;
