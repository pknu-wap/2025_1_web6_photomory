import React, { useState } from "react";
import addGroupButton from "../../assets/addGroupButton.svg";
import { createGroup } from "../../api/ourAlbumApi";
import "./AddGroupButton.css";
function AddGroupButton({ onAddGroup }) {
  const [showModal, setShowModal] = useState(false);
  const [groupInfo, setGroupInfo] = useState({
    groupName: "",
    groupDescription: "",
  });

  // 모달 열기 핸들러
  const handleOpenModal = () => setShowModal(true);

  // 모달 닫기 핸들러
  const handleCloseModal = () => setShowModal(false);

  //그룹명 입력 핸들러
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setGroupInfo((prev) => ({ ...prev, [name]: value }));
  };
  // 그룹명 제출 핸들러
  const handleSubmit = async () => {
    const trimmedName = groupInfo.groupName.trim().replace(/\s+/g, "");
    const trimmedDesc = groupInfo.groupDescription.trim();

    if (trimmedName === "")
      return alert("그룹명을 입력해주세요. (공백만 입력 불가)");
    // 그룹 객체 생성 (멤버는 나중에 추가한다고 가정)
    try {
      const result = await createGroup({
        groupName: trimmedName,
        groupDescription: trimmedDesc,
      });
      onAddGroup(result);
    } catch (error) {
      console.error("그룹 생성 실패:", error);
      alert("그룹 생성 중 오류가 발생했습니다.");
      return;
    }

    setGroupInfo({ groupName: "", groupDescription: "" });
    handleCloseModal();
  };
  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
      }}
    >
      <button onClick={handleOpenModal} className="custom-hover-button">
        <img src={addGroupButton} alt="그룹 추가 아이콘" />
        <span style={{ fontSize: "16px", color: "#333" }}>새 그룹 만들기</span>
      </button>

      {/* 모달창 (조건부 렌더링) */}
      {showModal && (
        <div className="modalContainer">
          <h3 className="modalTitle">새 그룹 만들기</h3>
          <label htmlFor="groupName" className="modalLabel">
            그룹이름
          </label>
          <input
            id="groupName"
            type="text"
            placeholder="그룹명을 입력하세요"
            value={groupInfo.groupName}
            onChange={handleInputChange}
            className="modalInput"
          />
          <label htmlFor="groupDescription" className="modalLabel">
            그룹 설명
          </label>
          <input
            id="groupDescription"
            name="groupDescription"
            type="text"
            placeholder="그룹 설명을 입력하세요"
            value={groupInfo.groupDescription}
            onChange={handleInputChange}
            className="modalInput"
          />
          <div className="modalButtonArea">
            <button onClick={handleCloseModal} className="cancelModalButton">
              취소
            </button>
            <button
              style={{
                width: "125.56px",
                height: "48px",
                borderRadius: "8px",
                background: "#000000",
                color: "#ffffff",
                cursor: "pointer",
              }}
              onClick={handleSubmit}
            >
              그룹 만들기
            </button>
          </div>
        </div>
      )}

      {/* 모달 외부 어두운 배경 */}
      {showModal && <div onClick={handleCloseModal} className="modalOverlay" />}
    </div>
  );
}

export default AddGroupButton;
