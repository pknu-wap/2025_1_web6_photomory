import React, { useState } from "react";
import selectGroupButton from "../../assets/selectGroupButton.svg";
import "./SelectGroupOption.css";
function SelectGroupOptions({ groupList, onSelect }) {
  const [isOpen, setIsOpen] = useState(false);
  const handleSelect = (group_name) => {
    const fakeEvent = {
      //select동작과 유사하게 맞추기 위한 가짜 이벤트객체
      target: {
        value: group_name,
      },
    };
    onSelect(fakeEvent); // 기존 handleChange에 맞춤
    setIsOpen(false); // 드롭다운 닫기
    console.log(isOpen);
  };
  return (
    <div>
      {/* 드롭다운 토글 버튼 */}

      <img
        onClick={() => setIsOpen((prev) => !prev)}
        className="selectGroupButton"
        src={selectGroupButton}
        alt="그룹 선택 버튼"
      />

      {/* 드롭다운 메뉴 */}
      {isOpen && (
        <ul className="select-dropdown">
          {groupList.map((group) => (
            <li
              key={group.group_id}
              onClick={() => handleSelect(group.group_name)}
              className="select-option"
            >
              {group.group_name}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default SelectGroupOptions;
