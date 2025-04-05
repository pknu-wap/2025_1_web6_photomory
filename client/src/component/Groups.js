import React, { useState } from "react";
import PaginationBar from "./PaginationBar";
function Groups({ groupList, selectedGroupId }) {
  const [currentPage, setCurrentPage] = useState(1); //초기 페이지 번호

  const groupsPerPage = 4; //한 페이지당 그룹 수
  const totalPages = Math.ceil(groupList.length / groupsPerPage); //총 페이지 수
  const indexOfLastGroup = currentPage * groupsPerPage; //마지막 그룹
  const indexOfFirstGroup = indexOfLastGroup - groupsPerPage; //첫번째 그룹
  const currentGroups = groupList.slice(indexOfFirstGroup, indexOfLastGroup); //그룹 범위 배열

  const handlePageClick = (pageNum) => setCurrentPage(pageNum);

  return (
    <div
      style={{
        marginTop: "16px",
        borderRadius: "8px",
        background: "#fff",
        padding: "16px",
        width: "100%",
      }}
    >
      <h3 style={{ marginBottom: "16px" }}>그룹 관리</h3>

      {/* 2행 2열 */}
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "1fr 1fr",
          gridTemplateRows: "1fr 1fr",
          gap: "24px",
        }}
      >
        {Array(4)
          .fill(null)
          .map((_, i) => {
            const group = currentGroups[i] || null;
            const isSelected = group && group.id === selectedGroupId; //선택된 그룹만 강조 스타일 적용

            return (
              <div
                key={i}
                style={{
                  backgroundColor: group //그룹이 없을땐 비워둠
                    ? isSelected
                      ? "#dbeafe" //선택된 그룹 파란색 배경
                      : "#f0f0f0" //선택되지 않는 그룹 배경
                    : "transparent",
                  borderRadius: "8px",
                  padding: "16px",
                  minHeight: "100px",
                  border: group
                    ? isSelected
                      ? "2px solid #3b82f6" //선택된 그룹 파란 경계
                      : "1px solid #ddd" //선택되지 않는 그룹 경계
                    : "1px dashed #ccc", //그룹이 없는 스롯 점선 경계
                  boxShadow: isSelected ? "0 0 0 2px #60a5fa" : "none",
                }}
              >
                {group ? (
                  <>
                    <h4>{group.groupName}</h4>
                    <span>{`(${group.members.length}명)`}</span>
                    <div
                      style={{
                        display: "flex",
                        flexWrap: "nowrap",
                        gap: "8px",
                        marginTop: "8px",
                      }}
                    >
                      {group.members.map((member, idx) => (
                        <span
                          key={idx}
                          style={{
                            backgroundColor: "#e0e0e0",
                            padding: "4px 10px",
                            borderRadius: "20px",
                            fontSize: "14px",
                          }}
                        >
                          {member}
                        </span>
                      ))}
                    </div>
                  </>
                ) : (
                  <p style={{ color: "#aaa" }}>빈 그룹 슬롯</p>
                )}
              </div>
            );
          })}
      </div>
      {/*페이지네이션 바 */}
      <PaginationBar
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={handlePageClick}
      />
    </div>
  );
}

export default Groups;
