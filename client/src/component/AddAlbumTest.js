import React, { useState } from "react";
import AlbumTitleListTest from "./AlbumTitleListTest";
import addAlbumButton from "../assets/addAlbumButton.svg";
import "./AddAlbum.css";
//앨범 추가 컴포넌트
function AddAlbumTest({
  selectedGroupId,
  albumTitlesByGroup,
  setAlbumTitlesByGroup,
  setGroupAlbums,
}) {
  const [newAlbumData, setNewAlbumData] = useState({
    album_name: "", //제목
    album_description: "", //설명
  }); // 앨범 생성 폼의 입력값(제목, 설명)을 저장하는 객체

  // 현재 그룹에 해당하는 기존 앨범 제목 배열 (없으면 빈 배열)
  const currentAlbumTitles = albumTitlesByGroup[selectedGroupId] || [];
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

    const { album_name, album_description } = newAlbumData;

    // 입력값이 모두 있을 때만 실행
    if (album_name && album_description) {
      const updatedTitles = [...currentAlbumTitles, album_name];

      // 그룹별 앨범 제목 객체 업데이트
      setAlbumTitlesByGroup((prev) => ({
        ...prev,
        [selectedGroupId]: updatedTitles,
      }));
    }

    const newAlbum = {
      album_id: `album-${Date.now()}`, //임시 고유 ID
      album_name,
      album_description,
      album_makingtime: new Date().toISOString().slice(0, 10), //YYYY-MM-DD
      photos: [], //사진은 나중에 추가
    };

    setGroupAlbums((prev) => [...prev, newAlbum]);

    // 입력값 초기화
    setNewAlbumData({
      album_name: "",
      album_description: "",
    });
  };

  return (
    <div className="addAlbumCard">
      <div className="AddAlbumInner">
        <h3 style={{ marginBottom: "16px" }} className="AddAlbumtitle">
          앨범 추가
        </h3>
        <form onSubmit={handleSubmit}>
          <label htmlFor="albumTitle">앨범 제목</label>
          <input
            id="albumTitle"
            type="text"
            name="album_name"
            value={newAlbumData.album_name}
            onChange={handleChange}
            placeholder="앨범 제목을 입력하세요."
            className="albumTitleInput"
          />
          <label htmlFor="albumDescription">설명</label>
          <textarea
            id="albumDescription"
            name="album_description"
            value={newAlbumData.album_description}
            onChange={handleChange}
            rows={4}
            placeholder="앨범에 대한 설명을 입력하세요."
            className="albumDescription"
          />
          {/*앨범 제목 목록 컴포넌트*/}
          <AlbumTitleListTest albumTitles={currentAlbumTitles} />
          <button type="submit" className="addAlbumButton">
            <img src={addAlbumButton} alt="addAlbumButton" />
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddAlbumTest;
