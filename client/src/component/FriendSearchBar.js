import { useState } from "react";

function FriendSearchBar({ onSearch }) {
  const [keyword, setKeyword] = useState(""); //검색어 상태

  const handleChange = (e) => {
    const value = e.target.value;
    setKeyword(value);
    onSearch(value); // 상위에서 필터링 로직 수행
  };

  return (
    <div style={{ marginBottom: "16px" }}>
      <input
        type="text"
        placeholder="친구 이름을 검색하세요"
        value={keyword}
        onChange={handleChange}
        style={{
          width: "300px",
          padding: "8px 12px",
          fontSize: "14px",
          border: "1px solid #ccc",
          borderRadius: "6px",
        }}
      />
    </div>
  );
}

export default FriendSearchBar;
