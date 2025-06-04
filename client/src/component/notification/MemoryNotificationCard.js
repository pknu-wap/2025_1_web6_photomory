import { useState } from "react";
import "./MemoryNotificationCard.css";
import clock from "../../assets/clock.svg";
import PhotoModal from "../photo/PhotoModal";

function MemoryNotificationCard({ data }) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const { message, photoUrl, title, date } = data;

  const handleOpenModal = () => setIsModalOpen(true);
  const handleCloseModal = () => setIsModalOpen(false);

  // 모달에 넘길 photo 형식 맞추기
  const fakePhotoData = {
    photo_url: photoUrl,
    photo_name: title,
    photo_makingtime: date,
    albumTitle: "추억", // 임의 앨범명
  };
  return (
    <div className={`memoryCard ${isModalOpen ? "modal-open" : ""}`}>
      <div className="memoryTime">
        <img src={clock} alt="clock" />
        <span className="memoryMessage">{message}</span>
      </div>
      <div className="memoryContent">
        {photoUrl ? (
          <img src={photoUrl} alt={title} className="memoryImage" />
        ) : (
          <div className="memoryImagePlaceholder">Image</div>
        )}
        <div style={{ display: "flex", flexDirection: "column" }}>
          <p className="memoryTitle">{title}</p>
          <p className="memoryDate">{date}</p>
          <button className="memoryButton" onClick={handleOpenModal}>
            다시보기
          </button>
        </div>
      </div>
      {isModalOpen && (
        <PhotoModal
          albumId={null} // 삭제 기능 없으니 필요 없음
          photo={fakePhotoData}
          onClose={handleCloseModal}
        />
      )}
    </div>
  );
}

export default MemoryNotificationCard;
