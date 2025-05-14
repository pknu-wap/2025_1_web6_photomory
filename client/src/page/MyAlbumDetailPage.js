import { useParams } from "react-router-dom";
import { useState } from "react";
import Container from "../component/common/Container";
import Photos from "../component/photo/Photos";
import { getPhotoPeriod } from "../utils/getPhotoPeriod";
import PhotoInfo from "../component/photo/PhotoInfo";
import PhotoSubmit from "../component/photo/PhotoSubmit";
import getAlbumById from "../api/getAlbumById";
import Header from "../component/Header";
import Footer from "../component/Footer";
function MyAlbumDetailPage() {
  const { albumId } = useParams();
  const result = getAlbumById(parseInt(albumId), "private");
  //TODO: 서버 연동시 수정 필요
  const album = result?.album || {
    album_name: "(제목 없음)",
    photos: [],
  };

  //앨범 설명
  const description =
    result?.description || "목 데이터 기반이라서 아직 설명 추가를 못함";

  //앨범에 대한 사진 배열 상태
  const [photoList, setPhotoList] = useState(album.photos || []);

  //사진 추가 헨들러
  const handleAddPhoto = (newPhoto) => {
    setPhotoList((prev) => [newPhoto, ...prev]);
  };

  //사진 삭제 헨들러
  const handleDeltePhoto = (photo_id) => {
    setPhotoList((prev) => prev.filter((p) => p.photo_id !== photo_id));
  };

  const albumPeriod = getPhotoPeriod(photoList); //앨범 기간
  const albumTitle = album.album_name; // 앨범이름
  const Count = photoList.length; //사진갯수
  return (
    <>
      <Header />
      <Container
        style={{
          margin: "0 auto", // 좌우 가운데 정렬
          padding: "40px 24px", // 위아래/좌우 여백
        }}
      >
        <div
          style={{
            margin: "0px auto",
            width: "1056px",
          }}
        >
          <div
            style={{
              width: "1056px",
            }}
          >
            <Photos
              type="private"
              albumTitle={albumTitle}
              photoList={photoList}
              onDeltePhoto={handleDeltePhoto}
            />
            <PhotoInfo
              albumTitle={albumTitle}
              albumPeriod={albumPeriod}
              description={description}
              photoCount={Count}
            />
          </div>
          <PhotoSubmit handleAddPhoto={handleAddPhoto} />
        </div>
      </Container>
      <Footer />
    </>
  );
}

export default MyAlbumDetailPage;
