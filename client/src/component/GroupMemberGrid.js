import React from "react";

// 그룹 멤버를 2행 4열 그리드로 렌더링하는 컴포넌트
function GroupMemberGrid({ groupName, groupMembers = [] }) {
  const maxSlots = 8; //한 영역 당 최대 그룹 인원수수
  const filledMembers = [...groupMembers];

  // 부족한 슬롯은 null로 채움
  while (filledMembers.length < maxSlots) {
    filledMembers.push(null);
  }

  return (
    <div>
      <h2>{groupName}</h2>
      <div
        style={{
          margin: "0 auto 32px",
          width: "1056px",
          height: "424px",
          background: " #FFFFFF",
          border: "2px solid #000000",
          borderRadius: "8px",
        }}
      >
        <h3>그룹 멤버</h3>
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(4, 1fr)",
            gridTemplateRows: "repeat(2, 1fr)",
            gap: "12px",

            width: "1004px",
            height: "288px",
            margin: "0px auto",
          }}
        >
          {filledMembers.map((member, i) => (
            <div
              key={i}
              style={{
                weight: "239px",
                height: "136px",
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
      </div>
    </div>
  );
}

export default GroupMemberGrid;
