import React, { useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import ImageUploader from "./ImageUploader";
import "./SignupForm.css";
import isValidPassword from "../utils/isValidPassword";
import signupUser from "../api/signupuser";
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
    user_password_check: "", //비밀번호 확인용 필드
    user_photourl: null,
    user_job: "",
    user_equipment: "",
    user_introduction: "",
    user_field: "", // 활동 지역
  });

  //navigate 사용
  const navigate = useNavigate();

  // 이미지 리셋용 상태
  const [resetImage, setResetImage] = useState(false);

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

  //입력 폼 데이터 제출 헨들러
  const handleSubmit = async (e) => {
    //기본 이벤트 방지
    e.preventDefault();
    const { user_password } = signupData; //비밀번호 유효성 검사를 위한 구조분해

    if (signupData.user_password !== signupData.user_password_check) {
      alert("❗ 비밀번호가 일치하지 않습니다.");
      return;
    }

    if (!isValidPassword(user_password)) {
      alert(
        "❗ 비밀번호는 12자 이상, 숫자 4개 이상(중복 제외), 특수문자 2개 이상(중복 제외)이 포함되어야 합니다."
      );
      return;
    }

    //이미지 초기화
    setResetImage(true);
    const data = {
      user_name: signupData.user_name,
      user_email: signupData.user_email,
      user_password: signupData.user_password,
      user_job: signupData.user_job,
      user_equipment: signupData.user_equipment,
      user_introduction: signupData.user_introduction,
      user_field: signupData.user_field,
      user_photourl: null,
    };

    try {
      const result = await signupUser(data);

      if (result.message === "회원가입 완료") {
        // 성공했을 때만 초기화
        setSignupData({
          user_name: "",
          user_email: "",
          user_password: "",
          user_password_check: "",
          user_photourl: null,
          user_job: "",
          user_equipment: "",
          user_introduction: "",
          user_field: "",
        });

        navigate("/Signup/Confirm");
      } else if (result.message === "회원가입 실패(데이터베이스 오류 발생)") {
        alert("이미 존재하는 이메일입니다.");
      } else {
        alert("회원가입에 실패했습니다.");
      }
    } catch (error) {
      console.error("회원가입 중 오류:", error);
      alert("서버 오류로 회원가입에 실패했습니다.");
    }
  };

  // resetImage 상태를 업데이트하는 함수를 useCallback으로 감싸서 메모이제이션
  const handleResetImage = useCallback((value) => {
    // 부모 컴포넌트의 reset 상태를 true 또는 false로 설정
    setResetImage(value);
  }, []);

  return (
    <div className="signupWrapper">
      <div className="signupContainer">
        <h2 className="signupTitle">회원가입</h2>
        <form onSubmit={handleSubmit} className="signupForm">
          <label className="signupLabel">
            이름
            <input
              type="text"
              name="user_name"
              placeholder="이름을 입력하세요"
              value={signupData.user_name}
              onChange={handleChange}
              className="signupInput"
            />
          </label>

          <label className="signupLabel">
            이메일
            <input
              type="email"
              name="user_email"
              placeholder="이메일을 입력하세요"
              value={signupData.user_email}
              onChange={handleChange}
              className="signupInput"
            />
          </label>

          <label className="signupLabel">
            비밀번호
            <input
              type="password"
              name="user_password"
              placeholder="비밀번호를 입력하세요"
              value={signupData.user_password}
              onChange={handleChange}
              className="signupInput"
            />
          </label>
          <label className="signupLabel">
            비밀번호 확인
            <input
              type="password"
              name="user_password_check"
              placeholder="비밀번호를 입력하세요"
              value={signupData.user_password_check}
              onChange={handleChange}
              className="signupInput"
            />
          </label>
          {/*비밀번호가 다를때만 조건부 렌더링*/}
          {signupData.user_password_check &&
            signupData.user_password !== signupData.user_password_check && (
              <p style={{ color: "red", fontSize: "13px", margin: 0 }}>
                비밀번호가 일치하지 않습니다.
              </p>
            )}
          {/*이미지파일 입력 영역*/}
          <ImageUploader
            onFileSelect={handleImageSelect}
            value={signupData.user_photourl}
            reset={resetImage}
            onReset={handleResetImage}
          />

          {/* 직업 */}
          <div className="signupLabel">
            <p>직업</p>
            <select
              name="user_job"
              value={signupData.user_job}
              onChange={handleChange}
              className="signupSelect"
            >
              <option value="">직업 선택</option>
              {jobOptions.map((job) => (
                <option key={job} value={job}>
                  {job}
                </option>
              ))}
            </select>
          </div>

          {/* 사용 장비 */}
          <div className="signupLabel">
            <p>사용 장비</p>
            <select
              name="user_equipment"
              value={signupData.user_equipment}
              onChange={handleChange}
              style={{ marginBottom: "4px" }}
              className="signupSelect"
            >
              <option value="">사용 장비 선택</option>
              {equipmentOptions.map((equiment) => (
                <option key={equiment} value={equiment}>
                  {equiment}
                </option>
              ))}
            </select>
          </div>
          {/* 활동 지역 */}
          <div className="signupLabel">
            <p>활동 지역</p>
            <select
              name="user_field"
              value={signupData.user_field}
              onChange={handleChange}
              style={{ marginBottom: "22px" }}
              className="signupSelect"
            >
              <option value="">활동 지역 선택</option>
              {fieldOptions.map((field) => (
                <option key={field} value={field}>
                  {field}
                </option>
              ))}
            </select>
          </div>

          {/* 한 줄 소개 (자기소개) */}
          <div className="signupLabel">
            <p className="signupSubLabel">한 줄 소개</p>
            <input
              type="text"
              name="user_introduction"
              placeholder="한 줄 소개를 입력하세요"
              value={signupData.user_introduction}
              onChange={handleChange}
              className="signupInput signupIntro"
            />
          </div>

          <button type="submit" className="signupButton">
            회원가입
          </button>
        </form>
        {/*임시 p태그*/}
        <p className="signupLoginNotice">
          이미 계정이 있으신가요?{" "}
          <strong className="signupLoginLink">로그인 하기</strong>
        </p>
      </div>
    </div>
  );
}

export default SignupForm;
