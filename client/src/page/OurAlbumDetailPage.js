import { useParams } from "react-router-dom";
import getAlbumById from "../api/getAlbumById";
import GroupMemberGrid from "../component/GroupMemberGrid";
import Container from "../component/Container";
import Photos from "../component/Photos";
function OurAlbumDetailPage() {
  const { albumId } = useParams();
  const result = getAlbumById(albumId);

  if (!result) {
    return <p>앨범을 찾을 수 없습니다.</p>;
  }

  const { album, description, groupName, groupMembers } = result;

  return (
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
        <h2 style={{ marginBottom: "24px" }}>{groupName}</h2>
        <div
          style={{
            margin: "0 auto 32px",
            width: "1056px",
            height: "424px",
            background: " #FFFFFF",
            border: "2px solid #000000",
            borderRadius: "8px",
          }}
        >
          <h3>👥 그룹 멤버</h3>
          <GroupMemberGrid groupMembers={groupMembers} />
        </div>

        <div style={{ height: "128px" }}>
          <h2>#{album.title}</h2>
          <p>현재 보고 계신 앨범은 "{album.title}"태그의 사진들입니다.</p>
        </div>
        <div
          style={{
            width: "1056px",
            height: "1672px",
          }}
        >
          <Photos album={album} />
        </div>
        <p>{description}</p>
      </div>
    </Container>
  );
}

export default OurAlbumDetailPage;
