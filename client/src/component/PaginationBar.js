import React, { useState } from "react";
import "./PaginationBar.css";
import leftButton from "../assets/leftButton.svg";
import rightButton from "../assets/rightButton.svg";

function PaginationBar({ currentPage, totalPages, onPageChange }) {
  const [rangeIndex, setRangeIndex] = useState(0); //현재 보여주는 그룹 인덱스
  const buttonsPerPage = 4; // 한 번에 보여줄 버튼 개수

  const totalGroups = Math.ceil(totalPages / buttonsPerPage); // 버튼 그룹 수(한 그룹에 4개개)

  const pageNumbers = Array.from({ length: totalPages }, (_, i) => i + 1); //전체 페이지 갯수 만큼 번호 배열 생성

  // 현재 그룹의 시작/끝 인덱스 계산
  const start = rangeIndex * buttonsPerPage;
  const end = start + buttonsPerPage;
  const visiblePages = pageNumbers.slice(start, end); // 현재 보여줄 페이지만 추출

  const handlePrevGroup = () => {
    //4개의 범위를 이동
    if (rangeIndex > 0) {
      setRangeIndex(rangeIndex - 1);
    }
  };

  const handleNextGroup = () => {
    ////4개의 범위를 복귀
    if (rangeIndex < totalGroups - 1) {
      setRangeIndex(rangeIndex + 1);
    }
  };
  console.log("rangeIndex:", rangeIndex, "totalGroups:", totalGroups);
  return (
    <div style={{ textAlign: "center", marginTop: "16px" }}>
      <button
        onClick={handlePrevGroup}
        disabled={rangeIndex === 0}
        className="pagiNationNumbers"
      >
        <img
          src={leftButton}
          alt="leftButton"
          style={{
            opacity: rangeIndex === 0 ? 0.4 : 1, //비활성화시 투명도 조절절
            verticalAlign: "middle",
          }}
        />
      </button>
      {visiblePages.map((pageNum) => (
        <button
          key={pageNum}
          onClick={() => onPageChange(pageNum)}
          style={{
            backgroundColor: currentPage === pageNum ? "#000000" : "#eee",
            color: currentPage === pageNum ? "#fff" : "#333",
          }}
          className="pagiNationNumbers"
        >
          {pageNum}
        </button>
      ))}
      <button
        onClick={handleNextGroup}
        disabled={rangeIndex === totalGroups - 1}
        className="pagiNationNumbers"
      >
        <img
          src={rightButton}
          alt="rightButton"
          style={{
            opacity: rangeIndex === totalGroups - 1 ? 0.4 : 1, //비활성화시 투명도 조절
            verticalAlign: "middle",
          }}
        />
      </button>
    </div>
  );
}

export default PaginationBar;
