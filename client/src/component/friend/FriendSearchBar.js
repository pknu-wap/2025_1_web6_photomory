import { useState } from "react";
import searchIcon from "../../assets/searchIcon.svg";
import "./FriendSearchBar.css";
function FriendSearchBar({ onSearch }) {
  const [keyword, setKeyword] = useState(""); //검색어 상태

  //검색어 입력 헨들러
  const handleChange = (e) => {
    const value = e.target.value;
    setKeyword(value);
    onSearch(value); // 상위에서 필터링 로직 수행
  };

  return (
    <div className="friendSearchBarWrapper">
      <img src={searchIcon} alt="search" className="friendSearchIcon" />
      <input
        type="text"
        placeholder="친구 검색..."
        value={keyword}
        onChange={handleChange}
        className="friendSearchInput"
      />
    </div>
  );
}

export default FriendSearchBar;
