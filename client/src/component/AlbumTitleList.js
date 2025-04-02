//앨범 제목 목록 컴포넌트
function AlbumTitleList({ albumTitles }) {
  return (
    <>
      <h3>앨범 목록</h3>
      {/* 점 없애기, 기본 들여쓰기 제거 */}
      <ul style={{ listStyleType: "none", paddingLeft: 0 }}>
        {" "}
        {/* 앨범 제목들 하나씩 렌더링*/}
        {albumTitles.map((albumTitle, index) => (
          <li key={index}>
            <p>{`#${albumTitle}`}</p>
          </li>
        ))}
      </ul>
    </>
  );
}

export default AlbumTitleList;
