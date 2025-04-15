import React, { useState, useEffect, useRef } from "react";

//각 옵션들
const jobOptions = ["디자이너", "개발자", "사진작가", "프리랜서", "학생"];
const equipmentOptions = ["Canon", "Nikon", "Sony", "핸드폰 카메라"];
const fieldOptions = ["서울", "부산", "대전", "광주", "원주", "강릉", "기타"];

function SignupForm() {
  const [signupData, setSignupData] = useState({
    user_name: "",
    user_email: "",
    user_password: "",
    user_photourl: null,
    user_job: "",
    user_equipment: "",
    user_introduction: "",
    user_field: "", // 활동 지역
  });
  const [previewUrl, setPreviewUrl] = useState(null);
  const fileRef = useRef(null);

  //user_photourl을 제외한 입력한 입력값 헨들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    setSignupData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  //user_photourl 파일 입력값 헨들러
  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setSignupData((prev) => ({
      ...prev,
      user_photourl: file,
    }));

    if (file) {
      const url = URL.createObjectURL(file); //사진미리보기용 url
      setPreviewUrl(url); //미리보기 상태 변경
    } else {
      setPreviewUrl(null);
    }
  };

  //선택 이미지 초기화 헨들러
  const handleImageCancel = () => {
    setSignupData((prev) => ({
      ...prev,
      user_photourl: null,
    }));
    setPreviewUrl(null);

    //파일 input 초기화
    if (fileRef.current) {
      fileRef.current.value = "";
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const formData = new FormData();
    // 텍스트 필드 저장
    formData.append("user_name", signupData.user_name);
    formData.append("user_email", signupData.user_email);
    formData.append("user_password", signupData.user_password);
    formData.append("user_job", signupData.user_job);
    formData.append("user_equipment", signupData.user_equipment);
    formData.append("user_introduction", signupData.user_introduction);
    formData.append("user_field", signupData.user_field);

    // 파일 전송
    if (signupData.user_photourl) {
      formData.append("user_photourl", signupData.user_photourl);
    }

    console.log("FormData 준비 완료:");
    formData.forEach((value, key) => {
      if (value instanceof File) {
        console.log(`${key}: [파일 이름] ${value.name}`);
      } else {
        console.log(`${key}: ${value}`);
      }
    });

    //입력값 초기화
    setSignupData({
      user_name: "",
      user_email: "",
      user_password: "",
      user_photourl: null,
      user_job: "",
      user_equipment: "",
      user_introduction: "",
      user_field: "",
    });

    setPreviewUrl(null);

    if (fileRef.current) {
      fileRef.current.value = ""; //파일 input 창 비우기
    }
  };

  //이전 previewUrl 메모리 수동 해제
  useEffect(() => {
    return () => {
      if (previewUrl) {
        URL.revokeObjectURL(previewUrl);
      }
    };
  }, [previewUrl]);

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        name="user_name"
        placeholder="이름"
        value={signupData.user_name}
        onChange={handleChange}
      />
      <input
        type="email"
        name="user_email"
        placeholder="이메일"
        value={signupData.user_email}
        onChange={handleChange}
      />
      <input
        type="password"
        name="user_password"
        placeholder="비밀번호"
        value={signupData.user_password}
        onChange={handleChange}
      />

      <input
        type="file"
        name="user_photourl"
        accept="image/*"
        onChange={handleFileChange}
        ref={fileRef} ///ref연결
      />
      {/*이미지 미리보기 */}
      {previewUrl && (
        <div style={{ marginTop: "10px" }}>
          <p>이미지 미리보기</p>
          <img
            src={previewUrl}
            alt="previewUrl"
            style={{ width: "200px", borderRadius: "8px" }}
          />
          <br />
          <button type="button" onClick={handleImageCancel}>
            이미지 취소
          </button>
        </div>
      )}

      {/* 직업 */}
      <select
        name="user_job"
        value={signupData.user_job}
        onChange={handleChange}
      >
        <option value="">직업 선택</option>
        {jobOptions.map((job) => (
          <option key={job} value={job}>
            {job}
          </option>
        ))}
      </select>

      {/* 사용 장비 */}
      <select
        name="user_equipment"
        value={signupData.user_equipment}
        onChange={handleChange}
      >
        <option value="">사용 장비 선택</option>
        {equipmentOptions.map((equiment) => (
          <option key={equiment} value={equiment}>
            {equiment}
          </option>
        ))}
      </select>
      {/* 활동 지역 */}
      <select
        name="user_field"
        value={signupData.user_field}
        onChange={handleChange}
      >
        <option value="">활동 지역 선택</option>
        {fieldOptions.map((field) => (
          <option key={field} value={field}>
            {field}
          </option>
        ))}
      </select>
      {/* 한 줄 소개 (자기소개) */}
      <input
        type="text"
        name="user_introduction"
        placeholder="한 줄 소개를 입력하세요"
        value={signupData.user_introduction}
        onChange={handleChange}
      />

      <button type="submit">회원가입</button>
    </form>
  );
}

export default SignupForm;
