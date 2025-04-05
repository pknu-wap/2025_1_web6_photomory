import React from "react";

function PaginationBar({ currentPage, totalPages, onPageChange }) {
  const pageNumbers = Array.from({ length: totalPages }, (_, i) => i + 1); //전체 페이지 갯수 만큼 번호 배열 생성

  return (
    <div style={{ textAlign: "center", marginTop: "16px" }}>
      {pageNumbers.map((pageNum) => (
        <button
          key={pageNum}
          onClick={() => onPageChange(pageNum)}
          style={{
            margin: "0 4px",
            padding: "6px 12px",
            backgroundColor: currentPage === pageNum ? "#3b82f6" : "#eee",
            color: currentPage === pageNum ? "#fff" : "#333",
            border: "none",
            borderRadius: "4px",
            cursor: "pointer",
          }}
        >
          {pageNum}
        </button>
      ))}
    </div>
  );
}

export default PaginationBar;
