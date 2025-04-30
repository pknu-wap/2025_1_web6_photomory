import { Link } from "react-router-dom";
import greenCheck from "../assets/greenCheck.svg";
import "./SignupConfirmBox.css";
function SignupConfirmBox() {
  return (
    <div className="signupConfirmBox">
      <img src={greenCheck} alt="greenCheck" className="checkIcon" />
      <h2 className="signupTitle">회원가입이 완료 되었습니다!</h2>
      <p className="signupMessage">
        환영합니다! 이제 서비스의 모든 기능을 이용하실 수<br /> 있습니다.
      </p>
      {/* 메인 페이지 이동 */}
      <Link to="/" className="mainLink">
        메인 페이지로 이동
      </Link>

      {/* 로그인 페이지 이동 */}
      <Link to="/Longin" className="loginLink">
        로그인하기
      </Link>
    </div>
  );
}

export default SignupConfirmBox;
