import React, { useState } from "react";
import AlbumTitleList from "./AlbumTitleList";
//앨범 추가 컴포넌트
function AddAlbum({
  selectedGroupId,
  albumTitlesByGroup,
  setAlbumTitlesByGroup,
}) {
  const [newAlbumData, setNewAlbumData] = useState({
    title: "", //제목
    description: "", //설명
  }); // 앨범 생성 폼의 입력값(제목, 설명)을 저장하는 객체

  // 현재 그룹에 해당하는 기존 앨범 제목 배열 (없으면 빈 배열)
  const currentAlbumTitles = albumTitlesByGroup[selectedGroupId] || [];
  console.log(currentAlbumTitles);
  //앨범 내용 사용자 입력 상태 업데이트
  const handleChange = (e) => {
    const { name, value } = e.target;

    setNewAlbumData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };
  // 앨범 생성 처리
  const handleSubmit = (e) => {
    e.preventDefault();

    const { title, description } = newAlbumData;

    // 입력값이 모두 있을 때만 실행
    if (title && description) {
      const updatedTitles = [...currentAlbumTitles, title];

      // 그룹별 앨범 제목 객체 업데이트
      setAlbumTitlesByGroup((prev) => ({
        ...prev,
        [selectedGroupId]: updatedTitles,
      }));
    }

    // 입력값 초기화
    setNewAlbumData({
      title: "",
      description: "",
    });
  };

  return (
    <div style={{ width: "256px", height: "435px" }}>
      <h3>앨범 추가</h3>
      <form onSubmit={handleSubmit}>
        <label htmlFor="albumTitle">앨범 제목</label>
        <input
          id="albumTitle"
          type="text"
          name="title"
          value={newAlbumData.title}
          onChange={handleChange}
          placeholder="앨범 제목을 입력하세요."
          style={{ width: "100%", padding: "8px", marginBottom: "12px" }}
        />
        <label htmlFor="albumDescription">설명</label>
        <textarea
          id="albumDescription"
          name="description"
          value={newAlbumData.description}
          onChange={handleChange}
          placeholder="앨범에 대한 설명을 입력하세요."
          rows="4"
          style={{ width: "100%", padding: "8px", marginBottom: "12px" }}
        />
        <button type="submit" style={{ padding: "8px 16px" }}>
          앨범 생성
        </button>
      </form>
      {/*앨범 제목 목록 컴포넌트*/}
      <AlbumTitleList albumTitles={currentAlbumTitles} />
    </div>
  );
}

export default AddAlbum;
