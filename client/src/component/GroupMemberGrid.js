import React, { useState } from "react";
import PaginationBar from "./PaginationBar";

// 그룹 멤버를 2행 4열 그리드로 렌더링하는 컴포넌트
function GroupMemberGrid({ groupName, groupMembers = [] }) {
  const [currentPage, setCurrnetPage] = useState(0); //현재 페이지 상태태
  const membersPerPage = 8; //한 영역 당 최대 그룹 인원수

  // 전체 페이지 수 계산
  const totalPages = Math.ceil(groupMembers.length / membersPerPage);

  // 현재 페이지에 보여줄 멤버들 잘라내기
  const startIdx = currentPage * membersPerPage;
  const endIdx = startIdx + membersPerPage;
  const currentMembers = groupMembers.slice(startIdx, endIdx);

  const filledMembers = [...currentMembers]; //빈 공간 채우기용 배열

  // 부족한 슬롯은 null로 채움
  while (filledMembers.length < membersPerPage) {
    filledMembers.push(null);
  }

  const handlePageChange = (page) => {
    setCurrnetPage(page);
  };

  return (
    <div>
      <h2
        style={{
          fontSize: "24px",
          fontWeight: "bold",
          lineHeight: "32px",
          letterSpacing: "0px",
          marginBottom: "16px",
        }}
      >
        {groupName}
      </h2>
      <div
        style={{
          margin: "0 auto 32px",
          padding: "26px",
          width: "1056px",
          height: "424px",
          background: " #FFFFFF",
          border: "2px solid #000000",
          borderRadius: "8px",
          boxShadow:
            "0px 4px 6px -4px rgba(0, 0, 0, 0.1),0px 10px 15px -3px rgba(0, 0, 0, 0.1)",
        }}
      >
        <h3 style={{ marginBottom: "16px" }}>그룹 멤버</h3>
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
        <PaginationBar
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={handlePageChange}
        />
      </div>
    </div>
  );
}

export default GroupMemberGrid;
