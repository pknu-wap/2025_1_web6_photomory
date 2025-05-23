import React from "react";

function AllAlbumTags({ tags }) {
  return (
    <div style={{ marginTop: "24px" }}>
      <h3 style={{ fontSize: "16px", marginBottom: "12px" }}>전체 태그 목록</h3>
      <div style={{ display: "flex", flexWrap: "wrap", gap: "10px" }}>
        {tags.map((tag) => (
          <span
            key={tag}
            style={{
              backgroundColor: "#f0f0f0",
              color: "#333",
              padding: "6px 12px",
              borderRadius: "20px",
              fontSize: "14px",
            }}
          >
            #{tag}
          </span>
        ))}
      </div>
    </div>
  );
}

export default AllAlbumTags;
