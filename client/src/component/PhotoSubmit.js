import { useState, useRef } from "react";
import "./PhotoSubmit.css";
import submitFileImage from "../assets/submitFileImage.svg";
function PhotoSubmit({ handleAddPhoto }) {
  const [newPhotoData, setNewPhotoData] = useState({
    imgFile: null,
    photo_name: "",
    photo_makingtime: "",
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
    formData.append("photo_name", newPhotoData.photo_name);
    formData.append("photo_makingtime", newPhotoData.photo_makingtime);

    // 상위에 넘길 수 있도록 imgUrl 포함해서 넘김
    handleAddPhoto({
      photo_id: Date.now(),
      photo_name: newPhotoData.photo_name,
      photo_makingtime: newPhotoData.photo_makingtime,
      photo_url: URL.createObjectURL(newPhotoData.imgFile),
    }); //사진 목록 다시 추가 후 재렌더링

    //데이터 확인인
    console.log(
      "제출된 데이터:",
      formData.get("imgFile"),
      formData.get("photo_name"),
      formData.get("photo_makingtime")
    );

    resetForm(); // 제출 후 초기화
  };

  const resetForm = () => {
    //취소 후 초기화
    setNewPhotoData({
      imgFile: null,
      photo_name: "",
      photo_makingtime: "",
    });
    if (fileInputRef.current) {
      fileInputRef.current.value = ""; //브라우저 input 내부 파일 제거거
    }
  };

  return (
    <div className="photoSubmitContainer">
      <h2 className="photoSubmitTitle">새 사진 업로드</h2>
      <form onSubmit={handleSubmit} className="photoSubmitForm">
        <label htmlFor="upload" className="uploadLabel">
          <input
            type="file"
            name="imgFile"
            accept="image/*" //이미지 파일만 업로드 가능
            onChange={handleChange}
            ref={fileInputRef} //연결결
            required //값이 비어 있을 시 제출 안됨
            className="uploadInput"
          />
          {/*선택 파일이 없을 때때 아이콘 + 텍스트 묶어서 정중앙 정렬 */}
          {!newPhotoData.imgFile && (
            <div className="uploadPlaceholder">
              <img
                src={submitFileImage}
                alt="submitFileImage"
                className="uploadIcon"
              />
              <p className="uploadText">
                <strong
                  style={{
                    fontSize: "14px",
                    lineHeight: "20px",
                    textAlign: "center",
                    color: "#000000",
                  }}
                  className="strongUploadText"
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
            <p className="selectedFileName">
              선택된 파일: <strong>{newPhotoData.imgFile.name}</strong>
            </p>
          )}
        </label>
        <div className="inputFields">
          <label className="inputLabel">
            사진 제목
            <input
              type="text"
              name="photo_name"
              value={newPhotoData.photo_name}
              onChange={handleChange}
              placeholder="사진 제목 입력을 입력하세요"
              required
              className="textInput"
            />
          </label>

          <label className="inputLabel">
            촬영일
            <input
              type="date"
              name="photo_makingtime"
              value={newPhotoData.photo_makingtime}
              onChange={handleChange}
              required
              className="textInput"
            />
          </label>
        </div>
        <div className="buttonGroup">
          <button type="button" onClick={resetForm} className="cancelButton">
            취소
          </button>
          <button type="submit" className="submitButton">
            업로드
          </button>
        </div>
      </form>
    </div>
  );
}

export default PhotoSubmit;
