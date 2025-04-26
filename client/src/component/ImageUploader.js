import React, { useEffect, useRef, useState, useCallback } from "react";
import defaultProfile from "../assets/defaultProfileIcon.svg";

function ImageUploader({ onFileSelect, value, reset, onReset }) {
  const [previewUrl, setPreviewUrl] = useState(defaultProfile); //기본 미리보기 상태
  const fileRef = useRef(null);

  // 파일 선택 시
  const handleChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const url = URL.createObjectURL(file);
      setPreviewUrl(url);
      onFileSelect(file);
    }
  };

  // 이미지 취소, 리렌더링마다 불필요하게 재실행 방지
  const handleCancel = useCallback(() => {
    setPreviewUrl(defaultProfile); //미리보기 초기화
    onFileSelect(null);
    if (fileRef.current) {
      fileRef.current.value = "";
    }
  }, [onFileSelect]);

  //숨겨진 input 클릭 헨들러
  const handleUploadClick = () => {
    if (fileRef.current) {
      fileRef.current.click(); //강제 클릭 이벤트 발생, 숨겨진 input 클릭
    }
  };

  useEffect(() => {
    if (reset) {
      handleCancel();
      onReset(false);
    }
  }, [reset, handleCancel, onReset]); //항상 최신 상태의 함수를 참조

  return (
    <div style={{ marginBottom: "5px" }}>
      <p style={{ margin: "0px" }}>프로필 사진</p>
      <input
        type="file"
        accept="image/*"
        ref={fileRef}
        onChange={handleChange}
        style={{ display: "none" }} //기본 버튼 제거
      />
      <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
        <img
          src={previewUrl}
          alt="프로필 미리보기"
          style={{
            width: "96px",
            height: "96px",
            borderRadius: "50%",
            objectFit: "cover",
            marginTop: "10px",
          }}
        />
        <button
          type="button"
          onClick={handleUploadClick}
          style={{
            width: "101.88px",
            height: "38px",
            borderRadius: "6px",
            background: "#ffffff",
            border: "1px solid #000000",
          }}
        >
          사진 업로드
        </button>
        {previewUrl !== defaultProfile && (
          <button
            type="button"
            onClick={handleCancel}
            style={{
              width: "101.88px",
              height: "38px",
              borderRadius: "6px",
              background: "#ffffff",
              border: "1px solid #000000",
            }}
          >
            이미지 취소
          </button>
        )}
      </div>
    </div>
  );
}

export default ImageUploader;
