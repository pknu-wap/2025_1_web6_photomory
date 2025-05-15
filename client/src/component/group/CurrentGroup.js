import React, { useState, useEffect } from "react";
import AddGroupButton from "../add/AddGroupButton";
import SelectGroupOptions from "../group/SelectGroupOptions.js";
import "./CurrentGroup.css";

function CurrentGroup({ groupList, setSelectedGroupId, onAddGroup }) {
  const [selectedGroup, setSelectedGroup] = useState({
    group_id: "",
    group_name: "",
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
    const group = groupList.find((g) => g.group_name === name);
    setSelectedGroupId(group.group_id);
    setSelectedGroup((prev) => ({
      ...prev,
      group_id: group.group_id,
      group_name: group.group_name,
      members: group.members,
    }));
  };

  return (
    <div className="currentGroupCard">
      {selectedGroup && (
        <>
          {/*그룹명*/}
          <h4 className="groupTitle">{selectedGroup.group_name}</h4>
          <ul className="groupMembers">
            {selectedGroup.members.map((member, index) => (
              <li
                className="member"
                key={index}
                style={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  gap: "8px",
                }}
              >
                <img
                  src={member.user_photourl}
                  alt={member.user_name}
                  style={{ width: "24px", height: "24px", borderRadius: "50%" }}
                />
                <span>{member.user_name}</span>
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
