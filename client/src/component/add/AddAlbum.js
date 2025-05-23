import React, { useState } from "react";
import AlbumTitleList from "../album/AlbumTitleList";
import addAlbumButton from "../../assets/addAlbumButton.svg";
import "./AddAlbum.css";

const MAX_ALBUM_COUNT = 7; //최대 앨범 갯수

//앨범 추가 컴포넌트
function AddAlbum({
  type = "", //private | group
  selectedGroupId, //선택된 그룹 ID
  albumTitlesByGroup, //선택 그룹 앨범명 배열
  setAlbumTitlesByGroup, //선택 그룹 앨범명 상태 변화 함수
  setGroupAlbums, //그룹 앨범 추가
  albumTitles = [], //나만의 추억에서만 쓸 앨범명 목록
  setMyAlbums, //나만의 추억  앨범 상태 변화 함수
}) {
  const [newAlbumData, setNewAlbumData] = useState({
    album_name: "", //제목
    album_description: "", //설명
  }); // 앨범 생성 폼의 입력값(제목, 설명)을 저장하는 객체

  //타입이 "group"이면 그룹별 앨범명 배열, "private"이면 개인별 앨범명 배열 사용
  const currentAlbumTitles =
    type === "group" ? albumTitlesByGroup[selectedGroupId] || [] : albumTitles;

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
    if (!album_name || !album_description) {
      alert("앨범 제목과 설명을 모두 입력해주세요.");
      return;
    }

    // 현재 앨범 제목 목록과 현재 전체 앨범 개수 가져오기
    const currentTitles =
      type === "group"
        ? albumTitlesByGroup[selectedGroupId] || []
        : albumTitles;

    const currentAlbumCount =
      type === "group"
        ? albumTitlesByGroup[selectedGroupId]?.length || 0
        : albumTitles.length;

    // 최대 7개 제한
    if (currentAlbumCount >= MAX_ALBUM_COUNT) {
      alert("❗앨범은 최대 7개까지 생성할 수 있습니다.");
      return;
    }

    // 제목 중복 확인
    if (currentTitles.includes(album_name)) {
      alert("❗이미 존재하는 앨범 제목입니다.");
      return;
    }

    //새 앨범 객체
    const newAlbum = {
      album_id: `album-${Date.now()}`, //임시 고유 ID
      album_name,
      album_description,
      album_makingtime: new Date().toISOString().slice(0, 10), //YYYY-MM-DD
      photos: [], //사진은 나중에 추가
    };

    //타입에 따라 분기기
    if (type === "group") {
      // 그룹 앨범 추가 로직
      const updatedTitles = [
        ...(albumTitlesByGroup[selectedGroupId] || []),
        album_name,
      ];
      // 그룹 id별 앨범 제목 객체 업데이트
      setAlbumTitlesByGroup((prev) => ({
        ...prev,
        [selectedGroupId]: updatedTitles,
      }));

      setGroupAlbums((prev) => [...prev, newAlbum]);
    } else if (type === "private") {
      // 나만의 앨범 추가 로직
      setMyAlbums((prev) => [...prev, newAlbum]);
    }

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
          <AlbumTitleList albumTitles={currentAlbumTitles} />
          <button type="submit" className="addAlbumButton">
            <img src={addAlbumButton} alt="addAlbumButton" />
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddAlbum;
