import React from "react";
import SignupForm from "../component/SignupForm";
import "./SignupPage.css";

function SignupPage() {
  return (
    <div className="signupPageWrapper">
      {/* 회원가입 폼 */}
      <SignupForm />
    </div>
  );
}

export default SignupPage;
