import React, { useState, useEffect } from "react";
import { getMyAlbums } from "../api/getMyAlbum";
import Container from "../component/Container";
import CalenderTest from "../component/CalenderTest";
import Header from "../component/Header";
import AddAlbumTest from "../component/AddAlbumTest";
import AlbumList from "../component/AlbumList";
function MyAlbumPage() {
  const [myAlbums, setMyAlbums] = useState([]); //나의 앨범 상태

  //초기 렌더링시 배열로 앨범과 각 사진정보 받기
  useEffect(() => {
    const albums = getMyAlbums();
    setMyAlbums(albums);
  }, []);

  //앨범 제목만 뽑아낸 배열
  const albumTitles = myAlbums.map((album) => album.album_name);

  return (
    <>
      <Header />
      <Container
        style={{
          margin: "0 auto",
          padding: "0 40px",
          position: "relative",
          height: "2942px",
          opacity: "1",
        }}
      >
        <CalenderTest
          type="private" //개인 앨범용 타입
          myAlbums={myAlbums}
        />

        {/* 앨범 추가 오른쪽 영역을 가로 배치 */}
        <div style={{ display: "flex", gap: "24px", marginTop: "32px" }}>
          <AddAlbumTest
            type="private"
            albumTitles={albumTitles}
            setMyAlbums={setMyAlbums}
          />
          {/* 오른쪽 영역 */}
          <div style={{ flex: 1 }}>
            <div>
              {/*그룹별 앨범 목록을 보여주는 컴포넌트*/}
              <AlbumList albums={myAlbums} />
            </div>
          </div>
        </div>
      </Container>
    </>
  );
}

export default MyAlbumPage;
