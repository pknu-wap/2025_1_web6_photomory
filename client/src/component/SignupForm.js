import React, { useState } from "react";
import ImageUploader from "./ImageUploader";
//각 옵션들
const jobOptions = ["디자이너", "개발자", "사진작가", "프리랜서", "학생"];
const equipmentOptions = ["Canon", "Nikon", "Sony", "핸드폰 카메라"];
const fieldOptions = ["서울", "부산", "대전", "광주", "원주", "강릉", "기타"];

function SignupForm() {
  //회원가입 입력 정보 객체 상태
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

  //user_photourl을 제외한 입력한 입력값 헨들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    setSignupData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  //이미지 파일 선택 헨들러
  const handleImageSelect = (file) => {
    setSignupData((prev) => ({
      ...prev,
      user_photourl: file,
    }));
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
  };

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
      {/*이미지파일 입력 영역*/}
      <ImageUploader
        onFileSelect={handleImageSelect}
        value={signupData.user_photourl}
      />

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
