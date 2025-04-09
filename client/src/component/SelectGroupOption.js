import React, { useState } from "react";
import selectGroupButton from "../assets/selectGroupButton.svg";

function SelectGroupOptions({ groupList, onSelect }) {
  const [isOpen, setIsOpen] = useState(false);
  console.log(isOpen);
  const handleSelect = (groupName) => {
    const fakeEvent = {
      //select동작과 유사하게 맞추기 위한 가짜 이벤트객체
      target: {
        value: groupName,
      },
    };
    onSelect(fakeEvent); // 기존 handleChange에 맞춤
    setIsOpen(false); // 드롭다운 닫기
    console.log(isOpen);
  };
  console.log(isOpen);
  return (
    <div>
      {/* 드롭다운 토글 버튼 */}

      <img
        onClick={() => setIsOpen((prev) => !prev)}
        style={{ cursor: "pointer" }}
        src={selectGroupButton}
        alt="그룹 선택 버튼"
      />

      {/* 드롭다운 메뉴 */}
      {isOpen && (
        <ul
          style={{
            position: "absolute",
            marginTop: "4px",
            padding: "0",
            listStyle: "none",
            backgroundColor: "#fff",
            border: "1px solid #ccc",
            borderRadius: "8px",
            zIndex: 1000,
          }}
        >
          {groupList.map((group) => (
            <li
              key={group.id}
              onClick={() => handleSelect(group.groupName)}
              style={{
                padding: "8px 12px",
                cursor: "pointer",
                borderBottom: "1px solid #eee",
              }}
            >
              {group.groupName}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default SelectGroupOptions;
