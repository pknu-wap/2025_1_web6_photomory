import "./MemoryNotificationCard.css";
import clock from "../../assets/clock.svg";

function MemoryNotificationCard({ data }) {
  const { message, photoUrl, title, date } = data;
  return (
    <div className="memory-card">
      <div className="memory-dot">
        <img src={clock} alt="clock" />
        <span className="memory-message">{message}</span>
      </div>
      <div className="memory-content">
        {photoUrl ? (
          <img src={photoUrl} alt={title} className="memory-image" />
        ) : (
          <div className="memory-image-placeholder">Image</div>
        )}
        <div style={{ display: "flex", flexDirection: "column" }}>
          <p className="memory-title">{title}</p>
          <p className="memory-date">{date}</p>
          <button className="memory-button">다시보기</button>
        </div>
      </div>
    </div>
  );
}

export default MemoryNotificationCard;
