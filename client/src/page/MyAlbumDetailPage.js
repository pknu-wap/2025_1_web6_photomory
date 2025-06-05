import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import Container from "../component/common/Container";
import Photos from "../component/photo/Photos";
import { getPhotoPeriod } from "../utils/getPhotoPeriod";
import PhotoInfo from "../component/photo/PhotoInfo";
import PhotoSubmit from "../component/photo/PhotoSubmit";
import { getMyAlbumById } from "../api/getAlbumById";
import Header from "../component/common/Header";
import Footer from "../component/common/Footer";

function MyAlbumDetailPage() {
  const { albumId } = useParams();
  const [album, setAlbum] = useState(null); //앨범
  const [description, setDescription] = useState(""); //앨범명
  const [photoList, setPhotoList] = useState([]); //사진 배열
  const [currentPage, setCurrentPage] = useState(1); // 페이지 상태

  useEffect(() => {
    (async () => {
      try {
        //albumid를 통한 상세 앨범 조회
        const result = await getMyAlbumById(albumId);
        if (result && result.album) {
          setAlbum(result.album);
          setDescription(result.description);
          setPhotoList(result.album.photos || []);
        }
      } catch (error) {
        console.error("앨범 불러오기 실패:", error);
      }
    })();
  }, [albumId]);

  // 사진 추가 헨들러
  const handleAddPhoto = (newPhoto) => {
    setPhotoList((prev) => [newPhoto, ...prev]);
  };

  if (!album) return <div>로딩 중...</div>;

  const albumPeriod = getPhotoPeriod(photoList); //촬영기간
  const albumTitle = album.album_name; //앨범명
  const Count = photoList.length; //사진 갯수

  return (
    <>
      <Header />
      <Container style={{ margin: "0 auto", padding: "40px 24px" }}>
        <div style={{ margin: "0px auto", width: "1056px" }}>
          <div style={{ width: "1056px" }}>
            <Photos
              type="private"
              albumTitle={albumTitle}
              photoList={photoList}
              currentPage={currentPage}
              setCurrentPage={setCurrentPage}
              onDeltePhoto={(photo_id) =>
                setPhotoList((prev) =>
                  prev.filter((p) => p.photo_id !== photo_id)
                )
              }
            />
            <PhotoInfo
              albumTitle={albumTitle}
              albumPeriod={albumPeriod}
              description={description}
              photoCount={Count}
            />
          </div>
          <PhotoSubmit
            type="private"
            albumId={albumId}
            handleAddPhoto={handleAddPhoto}
          />
        </div>
      </Container>
      <Footer />
    </>
  );
}

export default MyAlbumDetailPage;
