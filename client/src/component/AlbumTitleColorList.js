import "./AlbumTitleColorList.css";

function AlbumTitleColorList({
  selectedAlbumTitles = [],
  albumColorsMap,
  albumDotColorsMap,
}) {
  return (
    <div
      style={{ marginLeft: "80px", marginTop: "15px", width: "fit-content" }}
    >
      <ul className="albumColorList">
        {selectedAlbumTitles.map((title, i) => (
          <li
            key={i}
            style={{
              backgroundColor: albumColorsMap[title],
            }}
            className="albumBackColor"
          >
            <span
              style={{
                backgroundColor: albumDotColorsMap[title], //매핑된 진한
              }}
              className="albumColorDot"
            ></span>
            <span>#{title}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AlbumTitleColorList;
