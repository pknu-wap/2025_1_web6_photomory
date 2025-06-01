import "./MemoryNotificationCard.css";
import clock from "../../assets/clock.svg";

function MemoryNotificationCard({ data }) {
  const { message, photoUrl, title, date } = data;
  return (
    <div className="memoryCard">
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
          <button className="memoryButton">다시보기</button>
        </div>
      </div>
    </div>
  );
}

export default MemoryNotificationCard;
