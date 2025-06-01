import "./AlbumTitleList.css";
// 앨범 제목 목록을 보여주는 컴포넌트
function AlbumTitleListTest({ albumTitles }) {
  return (
    <>
      <h3 style={{ marginBottom: "4px" }}>앨범 목록</h3>
      {/* 리스트 스타일 제거: 점 없애고 들여쓰기 제거 */}
      <ul className="titleList">
        {/* 앨범 제목들을 하나씩 렌더링 */}
        {albumTitles.map((albumTitle, index) => (
          <li key={index} className="title">
            <p style={{ marginLeft: "5px" }}>{`#${albumTitle}`}</p>
          </li>
        ))}
      </ul>
    </>
  );
}

export default AlbumTitleListTest;
