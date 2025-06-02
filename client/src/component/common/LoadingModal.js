import React from "react";
import "./LoadingModal.css";

function LoadingModal({ message = "업로드 중입니다..." }) {
  return (
    <div className="loadingModal">
      <div className="loadingBackdrop"></div>
      <div className="loadingSpinner">
        <p>{message}</p>
      </div>
    </div>
  );
}

export default LoadingModal;
