function AlbumList({ albums }) {
  if (!albums || albums.length === 0) {
    return <p>앨범이 없습니다.</p>;
  }

  return (
    <div>
      {albums.map((album) => (
        <div
          key={album.id}
          style={{
            border: "1px solid #ddd",
            borderRadius: "8px",
            padding: "16px",
            marginBottom: "24px",
            backgroundColor: "#fefefe",
          }}
        >
          <div style={{ display: "flex" }}>
            {/* 앨범 제목 */}
            <h4>#{album.title}</h4>
            <p
              style={{
                color: "#1a73e8",
                textDecoration: "underline",
                cursor: "pointer",
              }}
            >
              앨범 상세보기 (준비 중)
            </p>
            <p style={{ color: "#666", fontSize: "14px" }}>
              생성일: {album.createdAt}
            </p>
          </div>
          {/* 사진들 */}
          <div
            style={{
              display: "flex",
              flexWrap: "wrap",
              gap: "12px",
              marginTop: "12px",
            }}
          >
            {album.photos.map((photo) => (
              <div
                key={photo.id}
                style={{
                  width: "150px",
                  textAlign: "center",
                }}
              >
                <img
                  src={photo.imgUrl}
                  alt={photo.title}
                  style={{
                    width: "100%",
                    height: "100px",
                    objectFit: "cover",
                    borderRadius: "4px",
                    marginBottom: "4px",
                  }}
                />
                <div style={{ fontSize: "14px" }}>{photo.title}</div>
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}

export default AlbumList;
