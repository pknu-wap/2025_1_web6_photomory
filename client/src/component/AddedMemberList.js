const MAX_MEMBERS = 20; // 최대 인원 20명

function AddedMemberList({ addedMembers, onRemoveMember }) {
  return (
    <div style={{ marginTop: "24px" }}>
      {/* 제목 */}
      <h3
        style={{ marginBottom: "12px", fontSize: "20px", fontWeight: "bold" }}
      >
        현재 그룹 멤버
      </h3>

      {/* 인원수 표시 */}
      <p style={{ marginBottom: "16px", color: "#6B7280" }}>
        {addedMembers.length} / {MAX_MEMBERS}명
      </p>

      {/* 멤버 리스트 */}
      <ul style={{ display: "flex", flexWrap: "wrap", gap: "16px" }}>
        {addedMembers.map((member) => (
          <li
            key={member.user_id}
            style={{
              width: "120px",
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              border: "1px solid #ccc",
              borderRadius: "8px",
              padding: "12px",
            }}
          >
            <img
              src={member.profile_url}
              alt={member.user_name}
              style={{
                width: "80px",
                height: "80px",
                borderRadius: "50%",
                marginBottom: "8px",
              }}
            />
            <span style={{ fontWeight: "bold", fontSize: "14px" }}>
              {member.user_name}
            </span>
            <button onClick={() => onRemoveMember(member.user_id)}>❌</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AddedMemberList;
