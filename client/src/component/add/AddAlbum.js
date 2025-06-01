import React, { useState } from "react";
import AlbumTitleList from "../album/AlbumTitleList";
import { createGroupAlbum } from "../../api/ourAlbumApi";
import { normalizeGroupAlbum } from "../../utils/normalizers";
import "./AddAlbum.css";

const MAX_ALBUM_COUNT = 7; //최대 앨범 갯수

//앨범 추가 컴포넌트
function AddAlbum({
  type = "", // "private" | "group"
  selectedGroupId, //선택된 그룹 ID
  albumTitlesByGroup, //선택 그룹 앨범명 배열
  setAlbumTitlesByGroup, //선택 그룹 앨범명 상태 변화 함수
  setGroupAlbums, //그룹 앨범 추가
  albumTitles = [], //나만의 추억에서만 쓸 앨범명 목록
  setMyAlbums, //나만의 추억  앨범 상태 변화 함수
  handleAddTagClick, //우리의 추억 앨범 생성 시 태그 추가
}) {
  const [newAlbumData, setNewAlbumData] = useState({
    album_name: "", //제목
    album_description: "", //설명
    tags: [], //태그 목록 (옵션)
  }); // 앨범 생성 폼의 입력값(제목, 설명, 태그)을 저장하는 객체

  // 타입이 "group"이면 그룹별 앨범명 배열, "private"이면 개인별 앨범명 배열 사용
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
  const handleSubmit = async (e) => {
    e.preventDefault();

    const { album_name, album_description, tags } = newAlbumData;

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

    try {
      if (type === "group") {
        // 서버에 앨범 생성 요청
        const createdAlbum = await createGroupAlbum(selectedGroupId, {
          albumName: album_name,
          albumTags: tags,
          albumMakingTime: new Date().toISOString(),
          albumDescription: album_description,
        });

        // 응답값 정규화
        const normalizedAlbum = normalizeGroupAlbum(createdAlbum);

        // 그룹별 앨범 제목 업데이트
        const updatedTitles = [...currentTitles, album_name];
        setAlbumTitlesByGroup((prev) => ({
          ...prev,
          [selectedGroupId]: updatedTitles,
        }));

        // 그룹 앨범 목록에 추가
        setGroupAlbums((prev) => [...prev, normalizedAlbum]);

        //태그 추가 핸들러 (선택적으로 실행)
        handleAddTagClick?.(normalizedAlbum.album_tag);
      } else if (type === "private") {
        // 나만의 앨범 생성
        const newAlbum = {
          album_id: Date.now(), // 임시 ID
          album_name,
          album_description,
          album_makingtime: new Date().toISOString().slice(0, 10), //YYYY-MM-DD
          tags,
          photos: [],
        };
        setMyAlbums((prev) => [...prev, newAlbum]);
      }

      //입력값 초기화
      setNewAlbumData({
        album_name: "",
        album_description: "",
        tags: [],
      });
    } catch (error) {
      console.error("앨범 생성 오류:", error);
      alert("앨범 생성 중 문제가 발생했습니다.");
    }
  };

  return (
    <div className="addAlbumCard">
      <div className="AddAlbumInner">
        <h3 className="AddAlbumtitle">앨범 추가</h3>
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
            앨범 만들기
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddAlbum;
