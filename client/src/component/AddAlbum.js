import React, { useState } from "react";
import AlbumTitleList from "./AlbumTitleList";
//앨범 추가 컴포넌트
function AddAlbum() {
  const [newAlbumData, setNewAlbumData] = useState({
    title: "", //제목
    description: "", //설명
  }); // 앨범 생성 폼의 입력값(제목, 설명)을 저장하는 객체
  const [AlbumTitles, setAlbumTitles] = useState([]); // 앨범 이름 목록 리스트트

  //앨범 내용 사용자 입력 상태 업데이트
  const handleChange = (e) => {
    const { name, value } = e.target;

    setNewAlbumData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("앨범 생성:", newAlbumData);

    if (newAlbumData.title && newAlbumData.description) {
      setAlbumTitles((prev) => [...prev, newAlbumData.title]); // 입력 내용이 있을 때 앨범이름리스트에 추가
    }
    // 입력초기화
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
      {/*앨범 목록 컴포넌트*/}
      <AlbumTitleList albumTitles={AlbumTitles} />
    </div>
  );
}

export default AddAlbum;
