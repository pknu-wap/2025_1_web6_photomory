import React, { useState, useEffect } from "react";
import AddGroupButton from "../add/AddGroupButton";
import SelectGroupOptions from "../group/SelectGroupOptions.js";
import defaultProfileIcon from "../../assets/defaultProfileIcon.svg";
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
      {/* 그룹 정보 영역 */}
      <div className="groupInfoArea">
        {selectedGroup && selectedGroup.group_id ? (
          <>
            {/* 그룹명 */}
            <h4 className="groupTitle">{selectedGroup.group_name}</h4>

            {/* 그룹 멤버 목록 */}
            <ul className="groupMembers">
              {selectedGroup.members.map((member, index) => (
                <li className="member" key={index}>
                  <img
                    src={member.user_photourl || defaultProfileIcon}
                    alt={member.user_name}
                    style={{
                      width: "24px",
                      height: "24px",
                      borderRadius: "50%",
                    }}
                  />
                  <span>{member.user_name}</span>
                </li>
              ))}
            </ul>
          </>
        ) : (
          <div className="noGroupPlaceholder">
            <p>👥 선택된 그룹이 없습니다.</p>
          </div>
        )}
      </div>

      {/* 그룹 선택/추가 옵션 */}
      <div className="groupActionAreaFixed">
        <SelectGroupOptions groupList={groupList} onSelect={handleChange} />
        <AddGroupButton onAddGroup={onAddGroup} />
      </div>
    </div>
  );
}

export default CurrentGroup;
