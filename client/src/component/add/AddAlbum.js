import React, { useState } from "react";
import AlbumTitleList from "../album/AlbumTitleList";
import { createGroupAlbum } from "../../api/ourAlbumApi";
import { normalizeGroupAlbum } from "../../utils/normalizers";
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
  handleAddTagClick, //우리의 추억 앨범 생성 시 태그 추가
}) {
  // 앨범 제목, 설명, 태그를 포함한 상태 객체
  const [newAlbumData, setNewAlbumData] = useState({
    album_name: "",
    album_description: "",
    tags: [], //공통 태그 필드
  });

  const [newTagInput, setNewTagInput] = useState(""); // 새 태그 입력값

  // 앨범명/설명 입력 처리
  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewAlbumData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // 새 태그 추가
  const handleAddNewTag = () => {
    const trimmed = newTagInput.trim();
    //이미 추가된 태그에 포함되지 않았을 때
    if (trimmed && !newAlbumData.tags.includes(trimmed)) {
      setNewAlbumData((prev) => ({
        ...prev,
        tags: [...prev.tags, trimmed],
      }));
    }
    setNewTagInput("");
  };

  // 태그 제거 버튼
  const handleRemoveTag = (tagToRemove) => {
    setNewAlbumData((prev) => ({
      ...prev,
      tags: prev.tags.filter((tag) => tag !== tagToRemove),
    }));
  };

  // 앨범 생성 처리
  const handleSubmit = async (e) => {
    e.preventDefault();
    const { album_name, album_description, tags } = newAlbumData;

    if (!album_name || !album_description) {
      alert("앨범 제목과 설명을 모두 입력해주세요.");
      return;
    }

    const currentTitles =
      type === "group"
        ? albumTitlesByGroup[selectedGroupId] || []
        : albumTitles;

    const currentAlbumCount =
      type === "group"
        ? albumTitlesByGroup[selectedGroupId]?.length || 0
        : albumTitles.length;

    if (currentAlbumCount >= MAX_ALBUM_COUNT) {
      alert("❗앨범은 최대 7개까지 생성할 수 있습니다.");
      return;
    }

    if (currentTitles.includes(album_name)) {
      alert("❗이미 존재하는 앨범 제목입니다.");
      return;
    }

    const newAlbum = {
      album_name,
      album_description,
      tags,
    };

    try {
      if (type === "group") {
        // 서버에 앨범 생성 요청
        const createdAlbum = await createGroupAlbum(selectedGroupId, {
          albumName: album_name,
          albumTags: tags,
          albumMakingTime: new Date().toISOString(),
          albumDescription: album_description,
        });

        const normalizedAlbum = normalizeGroupAlbum(createdAlbum);

        const updatedTitles = [
          ...(albumTitlesByGroup[selectedGroupId] || []),
          album_name,
        ];
        //그룹별 앨범 업데이트
        setAlbumTitlesByGroup((prev) => ({
          ...prev,
          [selectedGroupId]: updatedTitles,
        }));
        //해당 그룹 앨범 업데이트
        setGroupAlbums((prev) => [...prev, normalizedAlbum]);
        //해당 앨범의 태그 추가
        handleAddTagClick?.(normalizedAlbum.album_tag); // 선택적으로 존재할 경우 호출
      } else if (type === "private") {
        setMyAlbums((prev) => [...prev, newAlbum]);
      }

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

  // 현재 앨범 제목 목록
  const currentAlbumTitles =
    type === "group" ? albumTitlesByGroup[selectedGroupId] || [] : albumTitles;

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

          <label htmlFor="tagInput">태그 추가</label>
          <div className="tagContainer">
            <input
              id="tagInput"
              type="text"
              value={newTagInput}
              onChange={(e) => setNewTagInput(e.target.value)}
              placeholder="태그를 입력하세요"
              className="albumTagInput"
            />
            <button
              type="button"
              className="tagAddButton"
              onClick={handleAddNewTag}
            >
              +
            </button>
          </div>

          <div className="selectedTagList">
            {newAlbumData.tags.map((tag) => (
              <span key={tag} className="selectedTag">
                #{tag}
                <button
                  type="button"
                  onClick={() => handleRemoveTag(tag)}
                  className="cancelTagButton"
                >
                  ❌
                </button>
              </span>
            ))}
          </div>

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
