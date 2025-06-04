import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import Header from "../component/common/Header";
import GroupMemberGrid from "../component/group/GroupMemberGrid";
import Container from "../component/common/Container";
import Photos from "../component/photo/Photos";
import { getPhotoPeriod } from "../utils/getPhotoPeriod";
import PhotoInfo from "../component/photo/PhotoInfo";
import PhotoSubmit from "../component/photo/PhotoSubmit";
import Footer from "../component/common/Footer";
import {
  fetchGroupAlbumDetail,
  fetchGroupInfo,
  deleteGroupPost,
} from "../api/ourAlbumApi";
import { normalizeGroupAlbumDetail } from "../utils/normalizers";
function OurAlbumDetailPage() {
  const [photoList, setPhotoList] = useState([]); //앨범의 사진들 상태
  const [albumData, setAlbumData] = useState(null); // 전체 정규화 앨범범 데이터 저장
  const [currentPage, setCurrentPage] = useState(1); //현재 페이지
  const [isLastPage, setIsLastPage] = useState(false); //마지막 페이지 여부
  const size = 4; //한 페이지 당 4개 사진

  const { groupId, albumId } = useParams();

  //페이지 기반 전체 데이터 불러오기
  useEffect(() => {
    (async () => {
      try {
        const albumRes = await fetchGroupAlbumDetail(
          albumId,
          currentPage - 1,
          size
        );
        const groupRes = await fetchGroupInfo(groupId);
        const normalized = normalizeGroupAlbumDetail(albumRes, groupRes);
        setAlbumData(normalized); // 정규화된 전체 데이터 저장
        setPhotoList(normalized.album.photos); //사젠 데이터 추출

        //마지막 페이지 판단: 받아온 사진 수 < size
        setIsLastPage(normalized.album.photos.length < size);
      } catch (e) {
        console.error(e);
      }
    })();
  }, [groupId, albumId, currentPage]);

  useEffect(() => {
    const urlsToRevoke =
      photoList
        .filter((photo) => photo.photo_url?.startsWith("blob:"))
        .map((photo) => photo.photo_url) || [];

    return () => {
      // 페이지 나갈 때 blob URL 해제
      urlsToRevoke.forEach((url) => URL.revokeObjectURL(url));
    };
  }, [photoList]); // mount → unmount 시점에 한 번만 실행

  //사진 추가 헨들러
  const handleAddPhoto = (newPhoto) => {
    setPhotoList((prev) => [newPhoto, ...prev]);
  };

  //사진 삭제 헨들러
  const handleDeltePhoto = async (albumId, postId, photoId, photoName) => {
    try {
      const ok = await deleteGroupPost(albumId, postId);
      if (ok) {
        alert(`"${photoName}"사진을 삭제하였습니다.`);
        setPhotoList((prev) => prev.filter((p) => p.photo_id !== photoId));
      }
    } catch (error) {
      console.error("❗ 사진 삭제 중 오류:", error);
      alert("사진 삭제에 실패했습니다.");
    }
  };

  // 초기 상태 방어
  if (!albumData) {
    return <p>앨범 데이터를 불러오는 중입니다...</p>;
  }

  const { album, description, groupName, groupMembers } = albumData; //앨범 정보 구조 분해

  const albumPeriod = getPhotoPeriod(photoList); //앨범 기간
  const albumTitle = album.album_name; // 앨범이름
  const Count = photoList?.length ?? 0; //사진갯수
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
              albumId={albumId}
              currentPage={currentPage}
              setCurrentPage={setCurrentPage}
              isLastPage={isLastPage}
            />
            <PhotoInfo
              albumTitle={albumTitle}
              albumPeriod={albumPeriod}
              description={description}
              photoCount={Count}
            />
          </div>
          <PhotoSubmit
            type="group"
            albumId={albumId}
            handleAddPhoto={handleAddPhoto}
          />
        </div>
      </Container>
      <Footer />
    </>
  );
}

export default OurAlbumDetailPage;
