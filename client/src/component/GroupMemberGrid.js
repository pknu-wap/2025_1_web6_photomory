import React, { useState } from "react";
import PaginationBar from "./PaginationBar";
import "./GroupMemberGrid.css";

// 그룹 멤버를 2행 4열 그리드로 렌더링하는 컴포넌트
function GroupMemberGrid({ groupName, groupMembers = [] }) {
  const [currentPage, setCurrnetPage] = useState(1); //현재 페이지 상태태
  const membersPerPage = 8; //한 영역 당 최대 그룹 인원수

  // 전체 페이지 수 계산
  const totalPages = Math.ceil(groupMembers.length / membersPerPage);

  // 현재 페이지에 보여줄 멤버들 잘라내기
  const indexOfLastMember = currentPage * membersPerPage; //마지막멤버
  const indexOfFirstMember = indexOfLastMember - membersPerPage; //첫번째멤버
  const currentMembers = groupMembers.slice(
    indexOfFirstMember,
    indexOfLastMember
  ); //그룹 멤버 범위

  const filledMembers = [...currentMembers]; //빈 공간 채우기용 배열

  // 부족한 슬롯은 null로 채움
  while (filledMembers.length < membersPerPage) {
    filledMembers.push(null);
  }

  const handlePageChange = (page) => {
    setCurrnetPage(page);
  };

  return (
    <div>
      <h2 className="groupGridTitle">{groupName}</h2>
      <div className="groupGridContainer">
        <h3 className="groupGridSubtitle">그룹 멤버</h3>
        <div className="memberGrid">
          {filledMembers.map((member, i) => (
            <div
              key={member ? member.user_id : `empty-${i}`}
              className={`memberCard ${member ? "filled" : "empty"}`} //멤버 객체가 있을 때와 없을 때 각각에 따른 처치
            >
              {member ? (
                <img
                  src={member.profile_url}
                  alt="profile_url"
                  className="memberAvatar"
                />
              ) : null}
              {member ? member.user_name : "빈 슬롯"}
            </div>
          ))}
        </div>
        <PaginationBar
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={handlePageChange}
        />
      </div>
    </div>
  );
}

export default GroupMemberGrid;
