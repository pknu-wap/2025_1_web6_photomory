import { useState } from "react";
import CommentBox from "../common/CommentBox";
import dayjs from "dayjs";
import PhotoModal from "./PhotoModal";
import PaginationBar from "../common/PaginationBar";
import privateIcon from "../../assets/privateIcon.svg";
import PhotoGrid from "./PhotoGrid";
import "./Photos.css";
function Photos({ type, albumId, albumTitle, photoList = [], onDeltePhoto }) {
  const [selectedPhoto, setSelectedPhoto] = useState(null); //선택된 이미지 상태
  const [currentPage, setCurrentPage] = useState(1); //현재 페이지 상태

  let photosPerPage; //한 페이지당  사진 갯수

  if (type === "private") {
    photosPerPage = 8; //개인앨범일 때 한 페이지 당 8개의 사진
  } else {
    photosPerPage = 4; //그룹앨범일 때 한 페이지 당 4개의 사진
  }
  //모달 오픈 헨들러
  const handleOpenModal = (photo) => setSelectedPhoto({ ...photo, albumTitle }); //객체의 형태로 앨범명 추가
  //모달 닫기 헨들러
  const handleCloseModal = () => setSelectedPhoto(null);

  // 페이지 이동 핸들러
  const handlePageClick = (page) => {
    setCurrentPage(page);
  };

  // 전체 페이지 수 계산
  const totalPages = Math.ceil(photoList.length / photosPerPage);

  // 현재 페이지의 사진들
  const indexOfLastPhoto = currentPage * photosPerPage; //마지막앨범
  const indexOfFirstPhoto = indexOfLastPhoto - photosPerPage; //첫번째앨범
  const currentPhotos = photoList.slice(indexOfFirstPhoto, indexOfLastPhoto); //앨범 범위

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
        albumId={albumId} //사진 삭제시 사용되는 albumId
        photo={selectedPhoto}
        onClose={handleCloseModal}
        onDelete={onDeltePhoto}
      />

      {/* 페이지네이션 바 */}
      <PaginationBar
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={handlePageClick}
      />
    </div>
  );
}

export default Photos;
