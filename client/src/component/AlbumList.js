function AlbumList({ albumTitles }) {
  return (
    <>
      <h2>앨범 목록</h2>
      {/* 점 없애기, 기본 들여쓰기 제거 */}
      <ul style={{ listStyleType: "none", paddingLeft: 0 }}>
        {" "}
        {albumTitles.map(
          (
            albumTitle,
            index //앨범 제목들 하나씩 렌더링링
          ) => (
            <li key={index}>
              <p>{`#${albumTitle}`}</p>
            </li>
          )
        )}
      </ul>
    </>
  );
}

export default AlbumList;
