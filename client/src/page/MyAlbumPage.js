import React, { useState, useEffect } from "react";
import { getMyAlbums } from "../api/getMyAlbum";
import Container from "../component/Container";
import CalenderTest from "../component/CalenderTest";
import Header from "../component/Header";
import AddAlbumTest from "../component/AddAlbumTest";
import AlbumList from "../component/AlbumList";
import Footer from "../component/Footer";
import privateIcon from "../assets/privateIcon.svg";
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
          height: "2400px",
          opacity: "1",
          marginTop: "15px",
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
              <AlbumList albums={myAlbums} basePath="/my-album" />
            </div>
          </div>
        </div>
      </Container>
      <Footer />
    </>
  );
}

export default MyAlbumPage;
