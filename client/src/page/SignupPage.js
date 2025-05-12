import React from "react";
import SignupForm from "../component/SignupForm";
import "./SignupPage.css";
import Footer from "../component/Footer";
import Header from "../component/Header";

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
