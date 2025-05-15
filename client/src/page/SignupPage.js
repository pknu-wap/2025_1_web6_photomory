import React from "react";
import SignupForm from "../component/signup/SignupForm";
import Footer from "../component/common/Footer";
import Header from "../component/common/Header";

function SignupPage() {
  return (
    <div>
      {/* 회원가입 폼 */}
      <Header />
      <SignupForm />
      <Footer />
    </div>
  );
}

export default SignupPage;
