import React, { useState } from "react";

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

  const handleChange = (e) => {
    const { name, value } = e.target;
    setSignupData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setSignupData((prev) => ({
      ...prev,
      user_photourl: file,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("📝 입력한 회원가입 데이터:");
    for (const key in signupData) {
      if (key === "user_photourl") {
        console.log(`${key}:`, signupData[key]?.name || "없음");
      } else {
        console.log(`${key}:`, signupData[key]);
      }
    }
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
      <input
        type="file"
        name="user_photourl"
        accept="image/*"
        onChange={handleFileChange}
      />

      {/* 직업 */}
      <select
        name="user_job"
        value={signupData.user_job}
        onChange={handleChange}
      >
        <option value="">직업 선택</option>
        <option value="디자이너">디자이너</option>
        <option value="개발자">개발자</option>
        <option value="사진작가">사진작가</option>
        <option value="프리랜서">프리랜서</option>
        <option value="학생">학생</option>
      </select>

      {/* 사용 장비 */}
      <select
        name="user_equipment"
        value={signupData.user_equipment}
        onChange={handleChange}
      >
        <option value="">사용 장비 선택</option>
        <option value="Canon">Canon</option>
        <option value="Nikon">Nikon</option>
        <option value="Sony">Sony</option>
        <option value="핸드폰 카메라">핸드폰 카메라</option>
      </select>

      {/* 활동 지역 */}
      <select
        name="user_field"
        value={signupData.user_field}
        onChange={handleChange}
      >
        <option value="">활동 지역 선택</option>
        <option value="서울">서울</option>
        <option value="부산">부산</option>
        <option value="대전">대전</option>
        <option value="대전">광주</option>
        <option value="대전">원주</option>
        <option value="대전">강릉</option>
        <option value="기타">기타</option>
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
