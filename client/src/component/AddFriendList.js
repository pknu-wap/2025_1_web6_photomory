function AddFriendList({ friends }) {
  // 초대 버튼 클릭 핸들러
  const handleInviteClick = (friend) => {
    alert(`${friend.name}님을 초대했습니다!`);
  };

  return (
    <div
      style={{
        width: "500px",
        height: "818px",
        borderRadius: "8px",
        border: "1px solid #E5E7EB",
        padding: "13px",
        marginRight: "24px",
      }}
    >
      <h3
        style={{
          marginBottom: "16px",
          fontSize: "18px",
          fontWeight: "bold",
          lineHeight: "28px",
        }}
      >
        나의 친구 목록
      </h3>
      {friends.length > 0 ? (
        <ul
          style={{
            display: "flex",
            flex: "1",
            height: "90%",
            flexDirection: "column",
            gap: "16px",
            overflowY: "auto", // 세로 스크롤
            overflowX: "hidden", // 가로 스크롤 숨김
          }}
        >
          {friends.map((friend) => (
            <li
              key={friend.userId}
              style={{
                display: "flex",
                alignItems: "center",
                border: "none",
                borderRadius: "8px",
                padding: "12px",
                width: "450px",
                height: "72px",
                background: "#F3F4F6",
              }}
            >
              <img
                src={friend.photoUrl}
                alt={friend.name}
                style={{
                  width: "48px",
                  height: "48px",
                  borderRadius: "50%",
                  marginRight: "16px",
                }}
              />
              <span
                style={{
                  fontWeight: "bold",
                  textAlign: "center",
                  marginRight: "222px",
                }}
              >
                {friend.name}
              </span>
              {/* 초대하기 버튼 추가 */}
              <button
                style={{
                  padding: "4px 8px",
                  fontSize: "14px",
                  backgroundColor: "transparent",
                  color: "#000000",
                  cursor: "pointer",
                  width: "94px",
                  height: "44px",
                  borderRadius: "9999px",
                  border: "2px solid #000000",
                }}
                onClick={() => handleInviteClick(friend)}
              >
                초대하기
              </button>
            </li>
          ))}
        </ul>
      ) : (
        <div
          style={{
            height: "100%", // 빈 공간 전체 채우기
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            color: "#6B7280",
            fontSize: "16px",
          }}
        >
          친구를 찾을 수 없습니다
        </div>
      )}
    </div>
  );
}

export default AddFriendList;
