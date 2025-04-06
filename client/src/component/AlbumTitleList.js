// 앨범 제목 목록을 보여주는 컴포넌트
function AlbumTitleList({ albumTitles }) {
  return (
    <>
      <h3 style={{ marginBottom: "4px" }}>앨범 목록</h3>
      {/* 리스트 스타일 제거: 점 없애고 들여쓰기 제거 */}
      <ul
        style={{
          listStyleType: "none",
          paddingLeft: 0,
          marginBottom: "10px",
          maxHeight: "120px", // 최대 높이 지정 (넘어가면 스크롤됨)
          overflowY: "auto", // 세로 스크롤 추가
          borderRadius: "4px",
        }}
      >
        {/* 앨범 제목들을 하나씩 렌더링 */}
        {albumTitles.map((albumTitle, index) => (
          <li
            key={index}
            style={{
              background: "#F3F4F6",
              borderRadius: "9999px",
              marginBottom: "8px",
              height: "32px",
              display: "flex",
              flexDirection: "column",
              justifyContent: "center",
            }}
          >
            <p style={{ marginLeft: "5px" }}>{`#${albumTitle}`}</p>
          </li>
        ))}
      </ul>
    </>
  );
}

export default AlbumTitleList;
