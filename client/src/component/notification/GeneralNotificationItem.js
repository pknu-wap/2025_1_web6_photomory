import "./GeneralNotificationItem.css";
import defaultProfileIcon from "../../assets/defaultProfileIcon.svg";
import { acceptFriendRequest, rejectFriendRequest } from "../../api/friendApi";

function GeneralNotificationItem({ data }) {
  const { senderPhotourl, message, type, requestId } = data;

  const isFriendRequest = type === "FRIEND_REQUEST";

  //친구 요청 수락 헨들러
  const handleFriendsubmit = async () => {
    try {
      const ok = await acceptFriendRequest(requestId);
      if (ok) {
        alert("친구 요청을 수락했습니다.");
      }
    } catch (error) {
      alert("친구 요청 수락 중 오류 발생");
    }
  };

  const handleFriendRejectsubmit = async () => {
    try {
      await rejectFriendRequest(requestId);
      alert("친구 요청을 거절했습니다.");
    } catch (error) {
      alert("친구 요청 거절 중 오류 발생");
    }
  };

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
          <button className="btn accept" onClick={handleFriendsubmit}>
            수락
          </button>
          <button className="btn reject" onClick={handleFriendRejectsubmit}>
            거절
          </button>
        </div>
      )}
    </div>
  );
}

export default GeneralNotificationItem;
