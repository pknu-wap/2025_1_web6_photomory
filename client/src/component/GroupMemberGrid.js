import React from "react";

// 그룹 멤버를 2행 4열 그리드로 렌더링하는 컴포넌트
function GroupMemberGrid({ groupMembers = [] }) {
  const maxSlots = 8; //한 영역 당 최대 그룹 인원수수
  const filledMembers = [...groupMembers];

  // 부족한 슬롯은 null로 채움
  while (filledMembers.length < maxSlots) {
    filledMembers.push(null);
  }

  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: "repeat(4, 1fr)",
        gridTemplateRows: "repeat(2, 1fr)",
        gap: "12px",

        maxWidth: "600px",
        marginTop: "20px",
      }}
    >
      {filledMembers.map((member, i) => (
        <div
          key={i}
          style={{
            height: "100px",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            border: member ? "1px solid #ccc" : "2px dashed #bbb",
            borderRadius: "8px",
            fontWeight: "bold",
            backgroundColor: member ? "#fff" : "#f9f9f9",
            color: member ? "#333" : "#bbb",
          }}
        >
          {member || "빈 슬롯"}
        </div>
      ))}
    </div>
  );
}

export default GroupMemberGrid;
