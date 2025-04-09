import React from "react";
import "./PaginationBar.css";
function PaginationBar({ currentPage, totalPages, onPageChange }) {
  const pageNumbers = Array.from({ length: totalPages }, (_, i) => i + 1); //전체 페이지 갯수 만큼 번호 배열 생성

  return (
    <div style={{ textAlign: "center", marginTop: "16px" }}>
      {pageNumbers.map((pageNum) => (
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
    </div>
  );
}

export default PaginationBar;
