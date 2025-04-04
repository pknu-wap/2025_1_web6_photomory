function Groups({ groupList, selectedGroupId }) {
  // 최대 4개까지만 보여주기 (2행 2열 고정)
  // 4칸을 가진 배열에 각 그룹 넣음
  const fixedGroupSlots = Array(4)
    .fill(null)
    .map((_, i) => groupList[i] || null);

  return (
    <div
      style={{
        borderRadius: "8px",
        background: `linear-gradient(0deg, rgba(0, 0, 0, 0.001), rgba(0, 0, 0, 0.001)), #FFFFFF`,
      }}
    >
      <h3 style={{ marginBottom: "16px" }}>그룹 목록</h3>

      {/* 고정 2행 2열 그리드 */}
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "1fr 1fr", // 2열
          gridTemplateRows: "1fr 1fr", // 2행
          gap: "24px",
        }}
      >
        {/*4칸에 대한 렌더링*/}
        {fixedGroupSlots.map((group, idx) => {
          // 선택된 그룹인지 확인
          const isSelected = group && group.id === selectedGroupId; // 현재 칸의 그룹이 선택된 그룹인지 확인인

          return (
            <div
              key={idx}
              style={{
                backgroundColor: group //그룹이 먼저 존재 하는지 확인
                  ? isSelected
                    ? "#dbeafe" //선택된 그룹은 파란 배경
                    : "#f0f0f0" //아니면 회색
                  : "transparent", // 그룹이 없다면 투명
                borderRadius: "8px",
                padding: "16px",
                minHeight: "100px",
                border: group
                  ? isSelected
                    ? "2px solid #3b82f6" // 선택된 그룹 강조된 테두리
                    : "1px solid #ddd" //선택되지 않는 그룹 테두리
                  : "1px dashed #ccc", //그룹이 없다면 점선 테두리
                boxShadow: isSelected ? "0 0 0 2px #60a5fa" : "none", //선택된 그룹 테두리 강조
              }}
            >
              {/* 그룹 존재 확인*/}
              {group ? (
                <>
                  {/* 그룹명*/}
                  <h4 style={{ marginBottom: "8px" }}>{group.groupName}</h4>
                  <span>
                    {Array.isArray(group.members) &&
                      `(${group.members.length}명)`}
                  </span>
                  <div
                    style={{
                      display: "flex",
                      flexWrap: "wrap",
                      gap: "8px",
                    }}
                  >
                    {/* 그룹멤버*/}
                    {group.members.map((member, i) => (
                      <span
                        key={i}
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
                <p style={{ color: "#aaa" }}>빈 그룹 슬롯</p> //그룹 존재하지 않을 시 출력값
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}

export default Groups;
