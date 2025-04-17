const MAX_MEMBERS = 20; // 최대 인원 20명

function AddedMemberList({ addedMembers, onRemoveMember }) {
  // 삭제 클릭 핸들러
  const handleRemoveClick = (member) => {
    if (window.confirm(`${member.user_name}님을 삭제하시겠습니까?`)) {
      onRemoveMember(member.user_id);
    }
  };

  return (
    <div
      style={{
        width: "500px",
        height: "818px",
        border: "1px solid #E5E7EB",
        padding: "13px",
        borderRadius: "8px",
      }}
    >
      {/* 제목 */}
      <h3
        style={{
          marginBottom: "16px",
          fontSize: "18px",
          fontWeight: "bold",
          lineHeight: "28px",
        }}
      >
        현재 그룹 멤버
      </h3>

      {/* 인원수 표시 */}
      <p style={{ marginBottom: "16px", color: "#6B7280" }}>
        {addedMembers.length} / {MAX_MEMBERS}명
      </p>

      {/* 멤버 리스트 */}
      <ul
        style={{
          display: "flex",
          height: "85%",
          flexDirection: "column",
          gap: "16px",
          overflowY: "auto", // 세로 스크롤
          overflowX: "hidden", // 가로 스크롤 숨김
        }}
      >
        {addedMembers.map((member) => (
          <li
            key={member.user_id}
            style={{
              width: "450px",
              height: "72px",
              display: "flex",
              alignItems: "center",
              border: "none",
              borderRadius: "8px",
              padding: "12px",
              background: "#F3F4F6",
            }}
          >
            <img
              src={member.profile_url}
              alt={member.user_name}
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
                fontSize: "14px",
                marginRight: "275px",
              }}
            >
              {member.user_name}
            </span>
            <button
              onClick={() => handleRemoveClick(member)}
              style={{
                background: "none", // 배경 제거
                border: "none", // 테두리 제거
                color: "#EF4444",
                cursor: "pointer",
                padding: "4px 8px",
                fontSize: "14px",
              }}
              onMouseOver={(e) => (e.target.style.opacity = 0.7)}
              onMouseOut={(e) => (e.target.style.opacity = 1)}
            >
              삭제
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AddedMemberList;
