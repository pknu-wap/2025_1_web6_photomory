function AlbumTitleColorList({
  selectedAlbumTitles = [],
  albumColorsMap,
  albumDotColorsMap,
}) {
  return (
    <div style={{ marginTop: "16px" }}>
      <ul
        style={{
          paddingLeft: "16px",
          listStyleType: "none", // 점 없애기
          display: "flex", // 가로로 나열
          flexWrap: "wrap", // 줄바꿈 허용 (너무 길면 다음 줄로)
          gap: "8px", // 항목 사이 간격
          margin: 0, // 기본 마진 제거
        }}
      >
        {selectedAlbumTitles.map((title, i) => (
          <li
            key={i}
            style={{
              backgroundColor: albumColorsMap[title],
              borderRadius: "20px",
              padding: "6px 12px",
              fontSize: "14px",
              display: "flex",
              alignItems: "center",
              gap: "6px",
            }}
          >
            <span
              style={{
                width: "10px",
                height: "10px",
                backgroundColor: albumDotColorsMap[title], //매핑된 진한
                borderRadius: "50%",
                display: "inline-block",
              }}
            ></span>
            <span>#{title}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AlbumTitleColorList;
