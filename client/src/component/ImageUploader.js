// ImageUploader.js
import React, { useEffect, useRef, useState } from "react";

function ImageUploader({ onFileSelect, value }) {
  const [previewUrl, setPreviewUrl] = useState(null);
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

  // 이미지 취소 버튼 클릭
  const handleCancel = () => {
    setPreviewUrl(null);
    onFileSelect(null);
    if (fileRef.current) {
      fileRef.current.value = "";
    }
  };

  // 외부에서 value가 null로 바뀌면 자동 초기화
  useEffect(() => {
    if (!value) {
      setPreviewUrl(null);
      if (fileRef.current) {
        fileRef.current.value = "";
      }
    }
  }, [value]);

  return (
    <div>
      <input
        type="file"
        accept="image/*"
        ref={fileRef}
        onChange={handleChange}
      />
      {previewUrl && (
        <div>
          <img
            src={previewUrl}
            alt="미리보기"
            style={{ width: "200px", marginTop: "10px" }}
          />
          <br />
          <button type="button" onClick={handleCancel}>
            이미지 취소
          </button>
        </div>
      )}
    </div>
  );
}

export default ImageUploader;
