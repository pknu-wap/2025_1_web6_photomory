import { useState, useRef } from "react";
import "./PhotoSubmit.css";
import submitFileImage from "../../assets/submitFileImage.svg";
import { addPhotosToMyAlbum } from "../../api/myAlbumAPi";
function PhotoSubmit({ albumId, handleAddPhoto }) {
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

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("photos", newPhotoData.imgFile);

    //사진 메타데이터
    const photoMeta = [
      { name: newPhotoData.photo_name, date: newPhotoData.photo_makingtime },
    ];
    formData.append("photoData", JSON.stringify(photoMeta));

    try {
      // API 호출 (albumId와 함께)
      const result = await addPhotosToMyAlbum(albumId, formData);

      if (result) {
        console.log("서버에서 응답받은 데이터:", result);

        // 로컬 렌더링용 추가
        handleAddPhoto({
          photo_id: Date.now(),
          photo_name: newPhotoData.photo_name,
          photo_makingtime: newPhotoData.photo_makingtime,
          photo_url: URL.createObjectURL(newPhotoData.imgFile),
        });

        resetForm();
      } else {
        alert("서버 업로드에 실패했습니다.");
      }
    } catch (error) {
      alert("서버 요청 중 오류가 발생했습니다.");
      console.error(error);
    }
  };

  const resetForm = () => {
    //취소 후 초기화
    setNewPhotoData({
      imgFile: null,
      photo_name: "",
      photo_makingtime: "",
    });
    if (fileInputRef.current) {
      fileInputRef.current.value = ""; //브라우저 input 내부 파일 제거
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
