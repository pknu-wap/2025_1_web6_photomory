import { useState, useRef } from "react";
import "./PhotoSubmit.css";
import submitFileImage from "../assets/submitFileImage.svg";
function PhotoSubmit({ handleAddPhoto }) {
  const [newPhotoData, setNewPhotoData] = useState({
    imgFile: null,
    title: "",
    createdAt: "",
  });
  const fileInputRef = useRef(null); // 파일 input을 직접 제어하기 위한 ref

  const handleChange = (e) => {
    const { name, value, files } = e.target;

    if (name === "imgFile") {
      const file = files[0];
      setNewPhotoData((prev) => ({
        ...prev,
        imgFile: file,
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
      fileInputRef.current.value = ""; //브라우저 input 내부 파일 제거거
    }
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        background: "#ffffff",
        height: "395px",
        borderRadius: "8px",
        boxShadow:
          "0px 2px 4px -2px rgba(0, 0, 0, 0.1),0px 4px 6px -1px rgba(0, 0, 0, 0.1)",
        padding: "24px",
      }}
    >
      <h2 style={{ marginBottom: "16px" }}>새 사진 업로드</h2>
      <form
        onSubmit={handleSubmit}
        style={{
          width: "1008px",
          height: "246px",
        }}
      >
        <label
          htmlFor="upload"
          className="imageInput"
          style={{
            position: "relative",
            display: "inline-block", // 크기를 자식에 맞게 확장
            width: "1008px",
            height: "132px",
            borderRadius: "8px",
            border: "2px dashed #D1D5DB",
            overflow: "hidden", // 둥근 모서리 안 벗어나게
            cursor: "pointer",
            marginBottom: "24px",
          }}
        >
          <input
            type="file"
            name="imgFile"
            accept="image/*" //이미지 파일만 업로드 가능
            onChange={handleChange}
            ref={fileInputRef} //연결결
            required //값이 비어 있을 시 제출 안됨
            style={{
              width: "100%",
              height: "100%",
              opacity: 0, // 보이지 않게
              position: "absolute",
              top: 0,
              left: 0,
              cursor: "pointer",
            }}
          />
          {/*선택 파일이 없을 때때 아이콘 + 텍스트 묶어서 정중앙 정렬 */}
          {!newPhotoData.imgFile && (
            <div
              style={{
                position: "absolute",
                top: "50%",
                left: "50%",
                transform: "translate(-50%, -50%)",
                textAlign: "center",
                pointerEvents: "none", //이미지 영역 input 이벤트 막기 방지
              }}
            >
              <img
                src={submitFileImage}
                alt="submitFileImage"
                style={{
                  width: "48px",
                  height: "48px",
                }}
              />
              <p
                style={{
                  fontSize: "14px",
                  color: "#6B7280",
                  lineHeight: "20px",
                }}
              >
                <strong
                  style={{
                    fontSize: "14px",
                    lineHeight: "20px",
                    textAlign: "center",
                    color: "#000000",
                  }}
                >
                  파일 선택
                </strong>{" "}
                또는 드래그 앤 드롭
                <br />
                PNG, JPG, GIF 최대 10MB
              </p>
            </div>
          )}
          {newPhotoData.imgFile && (
            <p
              style={{
                fontSize: "14px",
                color: "#374151",
                marginTop: "8px",
              }}
            >
              선택된 파일: <strong>{newPhotoData.imgFile.name}</strong>
            </p>
          )}
        </label>
        <div style={{ display: "flex", gap: "12px", marginBottom: "30px" }}>
          <label
            style={{
              height: "57px",
              display: "flex",
              flexDirection: "column",
              gap: "4px",
              flex: "1",
              fontSize: "14px",
              fontWeight: "500",
              lineHeight: "20px",
            }}
          >
            사진 제목
            <input
              type="text"
              name="title"
              value={newPhotoData.title}
              onChange={handleChange}
              placeholder="사진 제목 입력을 입력하세요"
              required
              style={{
                height: "42px",
                borderRadius: "8px",
                border: "1px solid #6B7280",
                padding: "8px 12px",
                fontSize: "14px",
                outline: "none",
              }}
            />
          </label>

          <label
            style={{
              display: "flex",
              flexDirection: "column",
              flex: "1",
              gap: "4px",
              fontSize: "14px",
              fontWeight: "500",
              lineHeight: "20px",
              height: "42px",
            }}
          >
            촬영일
            <input
              type="date"
              name="createdAt"
              value={newPhotoData.createdAt}
              onChange={handleChange}
              required
              style={{
                height: "42px",
                borderRadius: "8px",
                border: "1px solid #6B7280",
                padding: "8px 12px",
                fontSize: "14px",
                outline: "none",
              }}
            />
          </label>
        </div>
        <div
          style={{ display: "flex", gap: "8px", justifyContent: "flex-end" }}
        >
          <button
            type="button"
            onClick={resetForm}
            style={{
              width: "63.45px",
              height: "42px",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              padding: "8px 16px",
              flexWrap: "wrap",
              background: "rgba(0, 0, 0, 0)",
              boxSizing: "border-box",
              border: "1px solid #E5E7EB",
              borderRadius: "8px",
              cursor: "pointer",
            }}
          >
            취소
          </button>
          <button
            type="submit"
            style={{
              width: "76.17px",
              height: "42px",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              padding: "8px 16px",
              flexWrap: "wrap",
              background: "#000000",
              boxSizing: "border-box",
              border: "1px solid #E5E7EB",
              borderRadius: "8px",
              cursor: "pointer",
              color: "#ffffff",
            }}
          >
            업로드
          </button>
        </div>
      </form>
    </div>
  );
}

export default PhotoSubmit;
