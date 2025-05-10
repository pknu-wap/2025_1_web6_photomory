import { useParams } from "react-router-dom";
import { useState } from "react";
import getAlbumByIdTest from "../api/getAlbumByIdTest";
import Header from "../component/Header";
import GroupMemberGrid from "../component/GroupMemberGrid";
import Container from "../component/Container";
import Photos from "../component/Photos";
import { getPhotoPeriod } from "../utils/getPhotoPeriod";
import PhotoInfo from "../component/PhotoInfo";
import PhotoSubmit from "../component/PhotoSubmit";
import Footer from "../component/Footer";
function OurAlbumDetailPageTest() {
  const { albumId } = useParams();
  const result = getAlbumByIdTest(Number(albumId), "group");
  console.log(result);
  const { album, description, groupName, groupMembers } = result; //앨범, 앨범설명, 그룹명, 그룹멤버

  const [photoList, setPhotoList] = useState(album.photos); //앨범의 사진들 상태

  if (!result) {
    return <p>앨범을 찾을 수 없습니다.</p>;
  }

  //사진 추가 헨들러
  const handleAddPhoto = (newPhoto) => {
    setPhotoList((prev) => [newPhoto, ...prev]);
  };

  //사진 삭제 헨들러
  const handleDeltePhoto = (photoId) => {
    setPhotoList((prev) => prev.filter((p) => p.photo_id !== photoId));
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
          height: "3650px",
        }}
      >
        <div
          style={{
            margin: "0px auto",
            width: "1056px",
          }}
        >
          <GroupMemberGrid groupName={groupName} groupMembers={groupMembers} />

          <div
            style={{
              width: "1056px",
            }}
          >
            <Photos
              type="group"
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

export default OurAlbumDetailPageTest;
