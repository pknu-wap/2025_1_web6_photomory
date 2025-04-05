import React, { useState } from "react";

function AddGroupButton({ onAddGroup }) {
  const [showModal, setShowModal] = useState(false); // 모달 표시 여부
  const [groupName, setGroupName] = useState(""); // 입력한 그룹 이름

  // 모달 열기 핸들러
  const handleOpenModal = () => setShowModal(true);

  // 모달 닫기 핸들러
  const handleCloseModal = () => setShowModal(false);

  //그룹명 입력 핸들러
  const handleInputChange = (e) => {
    setGroupName(e.target.value); // 입력값 상태 업데이트
  };

  // 그룹명 제출 핸들러
  const handleSubmit = () => {
    if (groupName.trim() === "") return alert("그룹명을 입력해주세요.");

    // 그룹 객체 생성 (멤버는 나중에 추가한다고 가정)
    const newGroup = {
      id: `group${Date.now()}`, // 고유 ID 생성
      groupName: groupName.trim(),
      members: [],
    };

    onAddGroup(newGroup); // 상위 컴포넌트로 전달
    setGroupName(""); //입력창 초기화
    handleCloseModal(); // 모달 닫기
  };
  return (
    <div>
      {/* 그룹 추가 버튼 */}
      <button onClick={handleOpenModal}>+ 그룹 추가</button>

      {/* 모달창 (조건부 렌더링) */}
      {showModal && (
        <div
          style={{
            position: "fixed",
            top: "50%",
            left: "50%",
            transform: "translate(-50%, -50%)",
            background: "#fff",
            padding: "20px",
            boxShadow: "0 2px 8px rgba(0,0,0,0.3)",
            zIndex: 1000,
          }}
        >
          <h3>새 그룹 만들기</h3>
          <input
            type="text"
            placeholder="그룹명을 입력하세요"
            value={groupName}
            onChange={handleInputChange}
            style={{ padding: "8px", width: "100%", marginBottom: "12px" }}
          />
          <div
            style={{ display: "flex", justifyContent: "flex-end", gap: "8px" }}
          >
            <button onClick={handleCloseModal}>취소</button>
            <button onClick={handleSubmit}>그룹 만들기</button>
          </div>
        </div>
      )}

      {/* 모달 외부 어두운 배경 */}
      {showModal && (
        <div
          onClick={handleCloseModal}
          style={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100vw",
            height: "100vh",
            background: "rgba(0, 0, 0, 0.4)",
            zIndex: 999,
          }}
        />
      )}
    </div>
  );
}

export default AddGroupButton;
