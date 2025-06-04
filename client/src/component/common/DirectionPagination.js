import React from "react";
import "./PaginationBar.css"; // 기존 스타일 재사용
import leftButton from "../../assets/leftButton.svg";
import rightButton from "../../assets/rightButton.svg";

function DirectionPagination({
  currentPage,
  onPrev,
  onNext,
  isFirstPage,
  isLastPage,
}) {
  return (
    <div
      style={{
        textAlign: "center",
        marginTop: "16px",
        display: "flex",
        justifyContent: "center",
        gap: "16px",
      }}
    >
      <button
        onClick={onPrev}
        disabled={isFirstPage}
        className="pagiNationNumbers"
      >
        <img
          src={leftButton}
          alt="leftButton"
          style={{
            opacity: isFirstPage ? 0.4 : 1,
            verticalAlign: "middle",
          }}
        />
      </button>

      <span style={{ lineHeight: "40px", fontWeight: "bold" }}>
        페이지 {currentPage}
      </span>

      <button
        onClick={onNext}
        disabled={isLastPage}
        className="pagiNationNumbers"
      >
        <img
          src={rightButton}
          alt="rightButton"
          style={{
            opacity: isLastPage ? 0.4 : 1,
            verticalAlign: "middle",
          }}
        />
      </button>
    </div>
  );
}

export default DirectionPagination;
