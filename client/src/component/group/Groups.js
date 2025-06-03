import { Link } from "react-router-dom";
import React, { useState } from "react";
import PaginationBar from "../common/PaginationBar";
import GroupControlButton from "../../assets/groupControlButton.svg";
import "./Groups.css";
function GroupsTest({ groupList, selectedGroupId }) {
  const [currentPage, setCurrentPage] = useState(1); //초기 페이지 번호

  const groupsPerPage = 4; //한 페이지당 그룹 수
  const totalPages = Math.ceil(groupList.length / groupsPerPage); //총 페이지 수
  const indexOfLastGroup = currentPage * groupsPerPage; //마지막 그룹
  const indexOfFirstGroup = indexOfLastGroup - groupsPerPage; //첫번째 그룹
  const currentGroups = groupList.slice(indexOfFirstGroup, indexOfLastGroup); //그룹 범위 배열

  const handlePageClick = (pageNum) => setCurrentPage(pageNum);

  return (
    <div className="allGroupCard">
      <div className="titleManagement">
        <h3>그룹 관리</h3>
        <Link
          to={`/our-album/${selectedGroupId}/groupEdit`}
          className="groupEditLink"
        >
          <img src={GroupControlButton} alt="GroupControlButton" />
        </Link>
      </div>
      {/* 2행 2열 */}
      <div className="fourGroupSection">
        {Array(4)
          .fill(null)
          .map((_, i) => {
            const group = currentGroups[i] || null;
            const isSelected = group && group.group_id === selectedGroupId; //선택된 그룹만 강조 스타일 적용

            return (
              <div
                key={i}
                style={{
                  backgroundColor: group //그룹이 없을땐 비워둠
                    ? isSelected
                      ? "#dbeafe" //선택된 그룹 파란색 배경
                      : "#f0f0f0" //선택되지 않는 그룹 배경
                    : "transparent",
                  border: group
                    ? isSelected
                      ? "2px solid #3b82f6" //선택된 그룹 파란 경계
                      : "1px solid #ddd" //선택되지 않는 그룹 경계
                    : "1px dashed #ccc", //그룹이 없는 스롯 점선 경계
                  boxShadow: isSelected ? "0 0 0 2px #60a5fa" : "none",
                }}
                className="groupcell"
              >
                {group ? (
                  <>
                    <div className="groupInfo">
                      <h4>{group.group_name}</h4>
                      <span>{`${group.members.length}명`}</span>
                    </div>
                    <div className="groupMembers">
                      {group.members.slice(0, 4).map((member, idx) => (
                        <span key={idx} className="groupMember">
                          {member.user_name}
                        </span>
                      ))}
                      {/* 초과 인원 있을 경우 +N명 표시 */}
                      {group.members.length > 4 && (
                        <span className="overCount">
                          +{group.members.length - 4}명
                        </span>
                      )}
                    </div>
                  </>
                ) : (
                  <p style={{ color: "#aaa" }}>빈 그룹 슬롯</p>
                )}
              </div>
            );
          })}
      </div>
      {/*페이지네이션 바 */}
      <PaginationBar
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={handlePageClick}
      />
    </div>
  );
}

export default GroupsTest;
