import "./AllAlbumTags.css";

function AllAlbumTags({ tags, selectedTags, onTagClick }) {
  return (
    <div className="allAlbumTagsContainer">
      <h3 className="allAlbumTagsTitle">전체 태그 목록</h3>
      <div className="allAlbumTagsList">
        {tags.map((tag) => (
          <button
            key={tag}
            className={`allAlbumTagItem ${
              selectedTags.includes(tag) ? "active" : ""
            }`}
            onClick={() => onTagClick(tag)}
          >
            #{tag}
          </button>
        ))}
      </div>
    </div>
  );
}

export default AllAlbumTags;
