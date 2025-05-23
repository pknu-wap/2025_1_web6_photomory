import { useParams } from "react-router-dom";
import { useState } from "react";
import getAlbumById from "../api/getAlbumById";
import Header from "../component/common/Header";
import GroupMemberGrid from "../component/group/GroupMemberGrid";
import Container from "../component/common/Container";
import Photos from "../component/photo/Photos";
import { getPhotoPeriod } from "../utils/getPhotoPeriod";
import PhotoInfo from "../component/photo/PhotoInfo";
import PhotoSubmit from "../component/photo/PhotoSubmit";
import Footer from "../component/common/Footer";
function OurAlbumDetailPage() {
  const { groupId, albumId } = useParams();
  const result = getAlbumById(Number(albumId), "group", Number(groupId));
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
