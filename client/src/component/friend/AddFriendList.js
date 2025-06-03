import "./AddFriendList.css";
import defaultProfileIcon from "../../assets/defaultProfileIcon.svg";
//친구 초대 목록 컴포넌트
function AddFriendList({ friends }) {
  //친구 초대 알림 헨들러
  const handleInviteClick = (friend) => {
    alert(`${friend.username}님을 초대했습니다!`);
  };

  return (
    <div className="addFriendContainer">
      <h3 className="addFriendTitle">나의 친구 목록</h3>
      {/*검색된 친구 없을시 안내 문구 출력*/}
      {friends.length > 0 ? (
        <ul className="friendList">
          {friends.map((friend) => (
            <li key={friend.userId} className="friendItem">
              <img
                src={friend.profileImageUrl || defaultProfileIcon}
                alt={friend.username}
                className="friendImage"
              />
              <span className="friendName">{friend.username}</span>
              <button
                className="inviteButton"
                onClick={() => handleInviteClick(friend)}
              >
                초대하기
              </button>
            </li>
          ))}
        </ul>
      ) : (
        <div className="noFriends">친구를 찾을 수 없습니다</div>
      )}
    </div>
  );
}

export default AddFriendList;
