function AlbumList({ albumTitles }) {
  if (albumTitles.length === 0) {
    return <p>아직 추가된 앨범이 없습니다.</p>;
  }

  return (
    <ul style={{ listStyleType: "none", paddingLeft: 0 }}>
      {" "}
      {/* 점 없애기, 기본 들여쓰기 제거 */}
      {albumTitles.map((albumTitle, index) => (
        <li key={index}>
          <p>{`#${albumTitle}`}</p>
        </li>
      ))}
    </ul>
  );
}

export default AlbumList;
