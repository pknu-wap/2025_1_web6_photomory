import React, { useState, useEffect } from "react";
import AddGroupButton from "./AddGroupButton";
import SelectGroupOptions from "./SelectGroupOption";
import "./CurrentGroup.css";

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
    <div className="currentGroupCard">
      {selectedGroup && (
        <>
          {/*그룹명*/}
          <h4 className="groupTitle">{selectedGroup.groupName}</h4>
          <ul className="groupMembers">
            {/*그룹멤버*/}
            {selectedGroup.members.map((member, index) => (
              <li className="member" key={index}>
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
