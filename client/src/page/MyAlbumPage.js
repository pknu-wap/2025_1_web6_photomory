import React, { useState, useEffect } from "react";
import { fetchMyMemoryAlbums } from "../api/myAlbumAPi";
import Container from "../component/common/Container";
import Calender from "../component/calender/Calender";
import Header from "../component/common/Header";
import AddAlbum from "../component/add/AddAlbum";
import AlbumList from "../component/album/AlbumList";
import Footer from "../component/common/Footer";
import privateIcon from "../assets/privateIcon.svg";
import AllAlbumTags from "../component/tag/AllAlbumTags";
import { normalizeMyAlbumData } from "../utils/normalizers";
function MyAlbumPage() {
  const [myAlbums, setMyAlbums] = useState([]); //나의 앨범 상태
  const [selectedTags, setSelectedTags] = useState([]); //선택된 태그 배열 상태

  //태그 선택 헨들러
  const handleTagClick = (tag) => {
    setSelectedTags(
      (prev) =>
        prev.includes(tag) ? prev.filter((t) => t !== tag) : [...prev, tag] //이미 선택된 태그 제거 or 태그 추가
    );
  };

  //선택된 앨범태그에 따른 앨범 필터링
  const filteredAlbums =
    selectedTags.length === 0
      ? myAlbums
      : myAlbums.filter((album) =>
          selectedTags.every((tag) => album.tags.includes(tag))
        );
  //초기 렌더링시 배열로 앨범과 각 사진정보 받기
  useEffect(() => {
    (async () => {
      try {
        const rawAlbums = fetchMyMemoryAlbums();

        const normalizedAlbums = rawAlbums.map(normalizeMyAlbumData); //정규화 처리
        setMyAlbums(normalizedAlbums);
      } catch (error) {
        console.log("앨범 불러오기 실패:", error);
        setMyAlbums([]);
      }
    })();
  }, []);

  //앨범 제목만 따로 추출한 배열
  const albumTitles = myAlbums.map((album) => album.album_name);

  // 모든 태그 중복 없이 추출
  const allTags = Array.from(new Set(myAlbums.flatMap((album) => album.tags)));

  return (
    <>
      <Header />
      <Container
        style={{
          margin: "28px auto 0",
          padding: "0 40px",
          position: "relative",
          height: "2400px",
          opacity: "1",
        }}
      >
        <Calender
          type="private" //개인 앨범용 타입
          myAlbums={myAlbums}
        />

        <div style={{ display: "flex", gap: "24px", marginTop: "32px" }}>
          {/* 왼쪽 영역: 앨범 생성 + 태그 */}
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              gap: "24px",
              width: "256px",
            }}
          >
            <AddAlbum
              type="private"
              albumTitles={albumTitles}
              setMyAlbums={setMyAlbums}
            />
            <AllAlbumTags
              tags={allTags}
              selectedTags={selectedTags}
              onTagClick={handleTagClick}
            />
          </div>

          {/* 오른쪽 영역 */}
          <div style={{ flex: 1 }}>
            <div
              style={{
                width: "340.17px",
                height: "80px",
                borderRadius: "8px",
                padding: "16px",
                background: "rgba(0, 0, 0, 0.1)",
                display: "flex",
                alignItems: "center",
                gap: "12px",
                marginBottom: "32px",
              }}
            >
              <img
                src={privateIcon}
                alt="privateIcon"
                style={{ width: "17.5px", height: "20px" }}
              />
              <div>
                <h2 style={{ fontSize: "18px" }}>나만의 추억 앨범</h2>
                <p style={{ fontSize: "14px" }}>
                  이 앨범은 나만 볼 수 있는 프라이빗 공간입니다.
                </p>
              </div>
            </div>
            <div>
              {/*개인 앨범 목록을 보여주는 컴포넌트*/}
              <AlbumList
                albums={filteredAlbums}
                type="private"
                basePath="/my-album"
              />
            </div>
          </div>
        </div>
      </Container>
      <Footer />
    </>
  );
}

export default MyAlbumPage;
