import { useState } from "react";
import CommentBox from "./CommentBox";
import dayjs from "dayjs";
import PhotoModal from "./PhotoModal";
import PaginationBar from "./PaginationBar";
import privateIcon from "../assets/privateIcon.svg";

function Photos({ type, albumTitle, photoList, onDeltePhoto }) {
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
      <div
        style={{
          height: "128px",
          background:
            "linear-gradient(0deg, rgba(0, 0, 0, 0.001), rgba(0, 0, 0, 0.001)), rgba(0, 0, 0, 0.1)",
          border: "2px solid rgba(0, 0, 0, 0.2)",
          borderRadius: "8px",
          padding: "26px",
          marginBottom: "56px",
        }}
      >
        <img
          src={privateIcon}
          alt="privateIcon"
          style={{ width: "25px", height: "25px", marginRight: "16px" }}
        />

        <h2
          style={{
            display: "inline-block", // 텍스트 길이에 맞게 박스 크기 자동 조절
            padding: "8px 16px",
            background: "#000000",
            color: "#ffffff", // 텍스트 색상 흰색
            borderRadius: "8px",
            fontSize: "20px",
            marginBottom: "12px",
          }}
        >
          #{albumTitle}
        </h2>
        <p>현재 보고 계신 앨범은 "{albumTitle}"태그의 사진들입니다.</p>
      </div>

      {currentPhotos.map((photo) => (
        <div
          key={photo.photo_id}
          style={{ display: "flex", gap: "24px", marginBottom: "24px" }}
        >
          <img
            src={photo.photo_url}
            alt={photo.photo_name}
            style={{
              borderRadius: "8px",
              width: "516px",
              height: "400px",
              cursor: "pointer",
              boxShadow:
                "0px 4px 6px -4px rgba(0, 0, 0, 0.1),0px 10px 15px -3px rgba(0, 0, 0, 0.1)",
            }}
            onClick={() => handleOpenModal(photo)}
          />
          <div>
            <h3
              style={{
                fontSize: "20px",
                fontWeight: "bold",
                lineHeight: "28px",
                letterSpacing: "0px",
                marginBottom: "8px",
              }}
            >
              {photo.photo_name}
            </h3>
            <p
              style={{
                fontSize: "14px",
                fontWeight: "normal",
                lineHeight: "20px",
                color: "#6B7280",
                marginBottom: "12px",
              }}
            >
              {dayjs(photo.photo_makingtime).format("YYYY/MM/DD")}
            </p>
            {/* 우리의 추억 페이지 일때만 조건부 렌더링 */}
            {type === "group" && <CommentBox />}
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
