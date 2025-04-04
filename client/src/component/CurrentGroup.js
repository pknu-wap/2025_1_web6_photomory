import React, { useState, useEffect } from "react";
import AddGroupButton from "./AddGroupButton";

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

  //select 값 변경 헨들러
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
        background: `linear-gradient(0deg, rgba(0, 0, 0, 0.001), rgba(0, 0, 0, 0.001)), #FFFFFF`,
        borderRadius: "8px",
        marginBottom: "39px",
      }}
    >
      {selectedGroup && (
        <>
          {/*그룹명*/}
          <h4>👥 {selectedGroup.groupName}</h4>
          <ul
            style={{
              listStyleType: "none", // 리스트 점 없애기
              display: "flex", // 가로로 나열
              gap: "8px", // 항목 사이 간격
              padding: 0, // 기본 들여쓰기 제거
              margin: 0, // 기본 마진 제거
            }}
          >
            {/*그룹멤버*/}
            {selectedGroup.members.map((member, index) => (
              <li key={index}>{member}</li>
            ))}
          </ul>
        </>
      )}
      {/* 그룹선택 옵션*/}
      <select value={"그룹 선택"} onChange={handleChange}>
        <option disabled value={"그룹 선택"}>
          그룹 선택
        </option>
        {groupList.map((group) => (
          <option key={group.id} value={group.groupName}>
            {group.groupName}
          </option>
        ))}
      </select>
      {/*그룹추가버튼 컴포넌트*/}
      <AddGroupButton onAddGroup={onAddGroup} />
    </div>
  );
}

export default CurrentGroup;
