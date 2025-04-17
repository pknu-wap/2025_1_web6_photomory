import { useState } from "react";
import searchIcon from "../assets/searchIcon.svg";

function FriendSearchBar({ onSearch }) {
  const [keyword, setKeyword] = useState(""); //검색어 상태

  const handleChange = (e) => {
    const value = e.target.value;
    setKeyword(value);
    onSearch(value); // 상위에서 필터링 로직 수행
  };

  return (
    <div
      style={{
        position: "relative",
        width: "100%",
        marginBottom: "16px",
      }}
    >
      <img
        src={searchIcon}
        alt="search"
        style={{
          position: "absolute",
          left: "12px", // 왼쪽 정렬
          top: "50%",
          transform: "translateY(-50%)",
          width: "16px",
          height: "16px",
          pointerEvents: "none",
          opacity: 0.6,
        }}
      />
      <input
        type="text"
        placeholder="친구 검색..."
        value={keyword}
        onChange={handleChange}
        style={{
          width: "100%",
          padding: "8px 12px 8px 36px",
          fontSize: "14px",
          border: "1px solid #ccc",
          borderRadius: "8px",
          height: "50px",
        }}
      />
    </div>
  );
}

export default FriendSearchBar;
