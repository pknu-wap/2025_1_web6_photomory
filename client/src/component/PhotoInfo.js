function PhotoInfo({ albumTitle, albumPeriod, description, photoCount }) {
  return (
    <div style={{}}>
      <h2>#{albumTitle}</h2>
      <p>{description}</p>
      <h2>촬영 정보</h2>
      <div
        style={{
          display: "flex",
          padding: "24px  24px 22px",
          gap: "380px",
          backgroundColor: "#ffffff",
          height: "94px",
          alignItems: "center",
        }}
      >
        <div>
          <p>촬영기간</p>
          <p>{albumPeriod}</p>
        </div>
        <div>
          <p>사진 매수</p>
          <p>{photoCount}매</p>
        </div>
      </div>
    </div>
  );
}

export default PhotoInfo;
