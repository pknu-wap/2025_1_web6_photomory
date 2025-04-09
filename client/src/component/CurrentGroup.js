import React, { useState, useEffect } from "react";
import AddGroupButton from "./AddGroupButton";
import SelectGroupOptions from "./SelectGroupOption";

function CurrentGroup({ groupList, setSelectedGroupId, onAddGroup }) {
  const [selectedGroup, setSelectedGroup] = useState({
    id: "",
    groupName: "",
    members: [],
  }); //선택된 그룹의 전체 객체

  //초기 렌더링 시 첫 번째 그룹 선택
  useEffect(() => {
    if (groupList.length > 0) {
      const firstGroup = groupList[0];
      setSelectedGroup(firstGroup);
    }
  }, [groupList]); //groupList가 바뀔 때마다 실행

  //선택 그룹룹 변경 헨들러
  const handleChange = (e) => {
    const name = e.target.value; // 선택된 <option>의 value 값 (즉, 그룹 이름)을 가져옴
    const group = groupList.find((g) => g.groupName === name);
    setSelectedGroupId(group.id);
    setSelectedGroup((prev) => ({
      ...prev,
      id: group.id,
      groupName: group.groupName,
      members: group.members,
    }));
  };

  return (
    <div
      style={{
        width: "1088px",
        height: "222px",
        background: `linear-gradient(0deg, rgba(0, 0, 0, 0.001), rgba(0, 0, 0, 0.001)), #FFFFFF`,
        boxShadow:
          "0px 2px 4px -2px rgba(0, 0, 0, 0.1),0px 4px 6px -1px rgba(0, 0, 0, 0.1)",
        borderRadius: "8px",
        marginBottom: "39px",
        padding: "36px 32px 48px",
      }}
    >
      {selectedGroup && (
        <>
          {/*그룹명*/}
          <h4
            style={{
              fontWeight: "500px",
              fontSize: "18px",
              lineHeight: "28px",
              marginBottom: "16px",
            }}
          >
            {selectedGroup.groupName}
          </h4>
          <ul
            style={{
              listStyleType: "none", // 리스트 점 없애기
              display: "flex", // 가로로 나열
              gap: "12px", // 항목 사이 간격
              padding: 0, // 기본 들여쓰기 제거
              marginBottom: "27px", // 기본 마진 제거
            }}
          >
            {/*그룹멤버*/}
            {selectedGroup.members.map((member, index) => (
              <li
                style={{
                  width: "90px",
                  height: "32px",
                  borderRadius: "9999px",
                  background: "rgb(243, 244, 246)",
                  display: "flex",
                  flexDirection: "row",
                  justifyContent: "end",
                  alignItems: "center",
                  padding: "4px 12px",
                  flexWrap: "wrap",
                  alignContent: "center",
                }}
                key={index}
              >
                {member}
              </li>
            ))}
          </ul>
        </>
      )}
      <div style={{ display: "flex", gap: "16px" }}>
        {/* 그룹선택 옵션*/}
        <SelectGroupOptions
          groupList={groupList}
          onSelect={handleChange} //그룹 선택 핸들러
        />

        {/*그룹추가버튼 컴포넌트*/}
        <AddGroupButton onAddGroup={onAddGroup} />
      </div>
    </div>
  );
}

export default CurrentGroup;
