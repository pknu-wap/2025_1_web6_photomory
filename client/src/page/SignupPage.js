import React from "react";
import SignupForm from "../component/SignupForm";
import "./SignupPage.css";

function SignupPage() {
  return (
    <div className="signupPageWrapper">
      <div className="signupPageInner">
        {/* 페이지 타이틀 */}
        <h1 className="signupPageTitle">Photomory에 가입하세요 📸</h1>

        {/* 회원가입 폼 */}
        <SignupForm />
      </div>
    </div>
  );
}

export default SignupPage;
