function MemoryNotificationCard({ data }) {
  const { message, photoUrl, title, date } = data;

  return (
    <div className="memory-card">
      <div className="memory-top">
        <span className="memory-dot">●</span>
        <span className="memory-message">{message}</span>
      </div>
      <div className="memory-image-wrapper">
        {photoUrl ? (
          <img src={photoUrl} alt={title} className="memory-image" />
        ) : (
          <div className="memory-image-placeholder">Image</div>
        )}
      </div>
      <div className="memory-content">
        <p className="memory-title">{title}</p>
        <p className="memory-date">{date}</p>
        <button className="memory-button">다시보기</button>
      </div>
    </div>
  );
}

export default MemoryNotificationCard;
