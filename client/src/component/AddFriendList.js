function AddFriendList({ friends }) {
  return (
    <div>
      <h3>나의 친구 목록</h3>
      <ul style={{ display: "flex", gap: "16px", flexWrap: "wrap" }}>
        {friends.map((friend) => (
          <li
            key={friend.userId}
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              border: "1px solid #ccc",
              borderRadius: "8px",
              padding: "12px",
              width: "120px",
              gap: "8px",
            }}
          >
            <img
              src={friend.photoUrl}
              alt={friend.name}
              style={{
                width: "80px",
                height: "80px",
                borderRadius: "50%",
              }}
            />
            <span style={{ fontWeight: "bold", textAlign: "center" }}>
              {friend.name}
            </span>
            {/* 초대하기 버튼 추가 */}
            <button
              style={{
                marginTop: "8px",
                padding: "4px 8px",
                fontSize: "14px",
                backgroundColor: "#3b82f6",
                color: "#fff",
                border: "none",
                borderRadius: "4px",
                cursor: "pointer",
              }}
            >
              초대하기
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AddFriendList;
