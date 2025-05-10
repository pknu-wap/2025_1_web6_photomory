import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import getAlbumById from "../api/getAlbumById";
import Header from "../component/Header";
import GroupMemberGrid from "../component/GroupMemberGrid";
import Container from "../component/Container";
import Photos from "../component/Photos";
import { getPhotoPeriod } from "../utils/getPhotoPeriod";
import PhotoInfo from "../component/PhotoInfo";
import PhotoSubmit from "../component/PhotoSubmit";
import Footer from "../component/Footer";
function OurAlbumDetailPage() {
  const { groupId, albumId } = useParams();

  // 그룹은 항상 존재, 앨범은 없을 수 있음
  const result = getAlbumById(Number(albumId), "group", Number(groupId));

  // 앨범 fallback 처리
  const album = result.album || {
    album_name: "(제목 없음)",
    photos: [],
  };

  // 설명도 fallback
  const description = result.description ?? "이 앨범에는 아직 설명이 없습니다.";

  const groupName = result.groupName;
  const groupMembers = result.groupMembers;

  // 사진 상태 관리
  const [photoList, setPhotoList] = useState(album.photos);

  useEffect(() => {
    setPhotoList(album.photos || []);
  }, [album]);

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
          height: "auto",
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

export default OurAlbumDetailPage;
