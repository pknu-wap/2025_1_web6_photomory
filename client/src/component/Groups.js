function Groups({ groupList, selectedGroupId }) {
  // 2행 2열(4개 단위)로 묶기, 각 묶음은 2행 2열로 표현 예정
  const groupChunks = [];
  for (let i = 0; i < groupList.length; i += 4) {
    groupChunks.push(groupList.slice(i, i + 4));
  }

  return (
    <div
      style={{
        overflowX: "auto", //가로 스크롤 허용
        marginTop: "16px",
        borderRadius: "8px",
        background: "#fff",
        width: "1088px",
      }}
    >
      <h3 style={{ paddingLeft: "8px" }}>그룹 관리</h3>

      {/* 가로 스크롤 가능한 래퍼 */}
      <div
        style={{
          display: "flex", //가로 정렬
          gap: "24px",
          padding: "8px 16px",
          minWidth: "max-content", // 내부 묶음들이 늘어나도 줄바꿈 없이 유지지
        }}
      >
        {groupChunks.map((chunk, chunkIndex) => (
          <div
            key={chunkIndex}
            style={{
              display: "grid", //2행*2열로 구성된 그리드드
              gridTemplateColumns: "1fr 1fr",
              gridTemplateRows: "1fr 1fr",
              gap: "24px",
              minWidth: "480px", // 한 묶음(4개) 기준 너비
            }}
          >
            {Array(4) //그룹이 부족할 경우 빈 슬롯 렌더링
              .fill(null)
              .map((_, i) => {
                const group = chunk[i] || null;
                const isSelected = group && group.id === selectedGroupId; //현재 선택된 그룹이면 강조 스타일 적용

                return (
                  <div
                    key={i}
                    style={{
                      backgroundColor: group
                        ? isSelected //선택 그룹일 시 강조된 배경색
                          ? "#dbeafe"
                          : "#f0f0f0"
                        : "transparent",
                      borderRadius: "8px",
                      padding: "16px",
                      minHeight: "100px",
                      border: group
                        ? isSelected //선택 그룹일 시 강조된 테두리색
                          ? "2px solid #3b82f6"
                          : "1px solid #ddd"
                        : "1px dashed #ccc",
                      boxShadow: isSelected ? "0 0 0 2px #60a5fa" : "none",
                      width: "270px", // 카드 고정 너비
                    }}
                  >
                    {group ? (
                      <>
                        <h4>{group.groupName}</h4>
                        <span>
                          {Array.isArray(group.members) &&
                            `(${group.members.length}명)`}
                        </span>
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
                      <p style={{ color: "#aaa" }}>빈 그룹 슬롯</p> //그룹이 없을 시 빈 슬롯
                    )}
                  </div>
                );
              })}
          </div>
        ))}
      </div>
    </div>
  );
}

export default Groups;
