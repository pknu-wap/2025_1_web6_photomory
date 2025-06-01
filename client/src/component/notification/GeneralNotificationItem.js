import "./GeneralNotificationItem.css";
import defaultProfileIcon from "../../assets/defaultProfileIcon.svg";
function GeneralNotificationItem({ data }) {
  const { senderPhotourl, message, type } = data;

  const isFriendRequest = type === "FRIEND_REQUEST";

  return (
    <div className="general-item">
      <img
        src={senderPhotourl || defaultProfileIcon}
        alt="sender"
        className="general-avatar"
      />
      <div className="general-message">{message}</div>

      {isFriendRequest && (
        <div className="general-actions">
          <button className="btn accept">수락</button>
          <button className="btn reject">거절</button>
        </div>
      )}
    </div>
  );
}

export default GeneralNotificationItem;
