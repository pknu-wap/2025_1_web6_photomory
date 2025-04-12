import { useParams } from "react-router-dom";
import { useState } from "react";
import getAlbumById from "../api/getAlbumById";
import GroupMemberGrid from "../component/GroupMemberGrid";
import Container from "../component/Container";
import Photos from "../component/Photos";
import { getPhotoPeriod } from "../utils/getPhotoPeriod";
import PhotoInfo from "../component/PhotoInfo";
import PhotoSubmit from "../component/PhotoSubmit";
function OurAlbumDetailPage() {
  const { albumId } = useParams();
  const result = getAlbumById(albumId);
  const { album, description, groupName, groupMembers } = result; //앨범, 앨범설명, 그룹명, 그룹멤버

  const [photoList, setPhotoList] = useState(album.photos); //앨범의 사진들 상태태

  if (!result) {
    return <p>앨범을 찾을 수 없습니다.</p>;
  }

  const handleAddPhoto = (newPhoto) => {
    setPhotoList((prev) => [newPhoto, ...prev]);
  };

  const albumPeriod = getPhotoPeriod(photoList); //앨범 기간
  const albumTitle = album.title; // 앨범이름
  const Count = photoList.length; //사진갯수
  return (
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
          <Photos albumTitle={albumTitle} photoList={photoList} />
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
  );
}

export default OurAlbumDetailPage;
