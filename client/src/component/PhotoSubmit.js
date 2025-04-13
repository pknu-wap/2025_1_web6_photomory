import { useState, useRef } from "react";

function PhotoSubmit({ handleAddPhoto }) {
  const fileInputRef = useRef(null); // 파일 input을 직접 제어하기 위한 ref

  const [newPhotoData, setNewPhotoData] = useState({
    imgFile: null,
    title: "",
    createdAt: "",
  });

  const handleChange = (e) => {
    const { name, value, files } = e.target;

    if (name === "imgFile") {
      setNewPhotoData((prev) => ({
        ...prev,
        imgFile: files[0], // 파일 객체 저장
      }));
    } else {
      setNewPhotoData((prev) => ({
        ...prev,
        [name]: value,
      }));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault(); //기본 동작(새로고침) 막기

    const formData = new FormData(); //파일 업로드할 때 사용하는 전용 데이터 객체
    formData.append("imgFile", newPhotoData.imgFile); //데이터 추가
    formData.append("title", newPhotoData.title);
    formData.append("createdAt", newPhotoData.createdAt);

    // 상위에 넘길 수 있도록 imgUrl 포함해서 넘김
    handleAddPhoto({
      id: Date.now(),
      title: newPhotoData.title,
      createdAt: newPhotoData.createdAt,
      imgUrl: URL.createObjectURL(newPhotoData.imgFile),
    }); //사진 목록 다시 추가 후 재렌더링

    //데이터 확인인
    console.log(
      "제출된 데이터:",
      formData.get("imgFile"),
      formData.get("title"),
      formData.get("createdAt")
    );

    resetForm(); // 제출 후 초기화
  };

  const resetForm = () => {
    //취소 후 초기화
    setNewPhotoData({
      imgFile: null,
      title: "",
      createdAt: "",
    });

    console.log(fileInputRef.current);
    if (fileInputRef.current) {
      fileInputRef.current.value = ""; //파일 input 초기화화
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      style={{
        display: "flex",
        flexDirection: "column",
        gap: "12px",
        width: "300px",
      }}
    >
      <label>
        이미지 파일
        <input
          type="file"
          name="imgFile"
          accept="image/*" //이미지 파일만 업로드 가능
          onChange={handleChange}
          ref={fileInputRef} //연결결
          required //값이 비어 있을 시 제출 안됨
        />
      </label>

      <label>
        사진 제목
        <input
          type="text"
          name="title"
          value={newPhotoData.title}
          onChange={handleChange}
          placeholder="사진 제목 입력"
          required //값이 비어 있을 시 제출 안됨
        />
      </label>

      <label>
        촬영일
        <input
          type="date"
          name="createdAt"
          value={newPhotoData.createdAt}
          onChange={handleChange}
          required //값이 비어 있을 시 제출 안됨
        />
      </label>

      <div style={{ display: "flex", gap: "8px", justifyContent: "flex-end" }}>
        <button type="button" onClick={resetForm}>
          취소
        </button>
        <button type="submit">업로드</button>
      </div>
    </form>
  );
}

export default PhotoSubmit;
