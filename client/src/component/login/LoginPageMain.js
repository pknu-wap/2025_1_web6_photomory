import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import styles from "./LoginPage.Main.module.css";
import loginImg from "../../assets/loginImg.svg";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEnvelope, faLock } from "@fortawesome/free-solid-svg-icons";
import logo from "../../assets/photomory_logo.svg";

const BASE_URL = process.env.REACT_APP_API_URL;

// 로그인 API 함수
export async function loginUser(email, password, navigate) {
  try {
    const response = await fetch(`${BASE_URL}/api/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        useremail: email,
        password: password,
      }),
      credentials: "include", //쿠키 방식 인증일 경우 필수
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || "이메일 또는 비밀번호가 잘못되었습니다.");
    }

    const data = await response.json();
    if (!data.accessToken) {
      alert("❌ accessToken이 없습니다!");
      return null;
    }

    localStorage.setItem("accessToken", data.accessToken);
    localStorage.setItem("refreshToken", data.refreshToken);
    localStorage.setItem("userName", data.userName);
    localStorage.setItem("userEmail", data.userEmail);

    return {
      userEmail: data.userEmail,
      userName: data.userName,
    };
  } catch (error) {
    alert(error.message); // 서버에서 보낸 텍스트 그대로 사용자에게 표시
    return null;
  }
}

export default function LoginPageMain() {
  const [email, setEmail] = useState("");
  const [pw, setPw] = useState("");
  const [error, setError] = useState();
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();
  const focusEmailRef = useRef();
  const focusPwRef = useRef();
  //로그인 여부, 사용자 이름 상태 변경함수 가져오기
  const { setIsLogged, setName } = useAuth();

  const onChangeHandleEmail = (e) => {
    setEmail(e.target.value);
    setError("");
  };
  const onChangeHandlePw = (e) => {
    setPw(e.target.value);
    setError("");
  };
  const onClickHandleSignUp = () => {};

  const onClickButtonLogin = async () => {
    setIsLoading(true);
    if (email === "") {
      focusEmailRef.current.focus();
      setError("이메일을 입력해주세요.");
      return;
    } else if (pw === "") {
      focusPwRef.current.focus();
      setError("비밀번호를 입력해주세요.");
      return;
    }
    const user = await loginUser(email, pw);
    if (user) {
      setIsLogged(true); //로그인 활성화
      setName(user.userName); // 사용자 이름 변경

      navigate("/Loged", {
        state: {
          userName: user.userName,
          userEmail: user.userEmail, //id는 이메일과 동일
        },
      });
    } else {
      //로그인 실패
      setEmail("");
      setPw("");
      focusEmailRef.current.focus();
    }

    setIsLoading(false); //로딩 종료
  };

  //     const onFocusHandle=(e)=>{ 온포커스 마루리하기
  //         if (e.className) {

  //         }
  //     }

  return (
    <>
      <div className={styles.loginPageMainContainer}>
        <img src={logo} className={styles.logoImage} alt="Photomy" />
        <div className={styles.loginContainer}>
          <p className={styles.loginText}>로그인</p>

          {/* 이메일 입력 */}
          <div className={styles.emailContainer}>
            <span>이메일</span>
            <div style={{ width: "100%", position: "relative" }}>
              <FontAwesomeIcon icon={faEnvelope} className={styles.emailIcon} />
              <input
                className={styles.emailInput}
                placeholder="이메일을 입력하세요."
                onChange={onChangeHandleEmail}
                value={email}
                disabled={isLoading}
                ref={focusEmailRef}
                onFocus={() => {}}
              />
            </div>
          </div>

          {/* 비밀번호 입력 */}
          <div className={styles.pwContainer}>
            <span className={styles.pwText}>비밀번호</span>
            <div style={{ width: "100%", position: "relative" }}>
              <FontAwesomeIcon icon={faLock} className={styles.pwIcon} />
              <input
                className={styles.pwInput}
                type="password"
                placeholder="비밀번호를 입력하세요."
                onChange={onChangeHandlePw}
                value={pw}
                disabled={isLoading}
                ref={focusPwRef}
              />
            </div>
          </div>

          {/* 에러 메시지 출력 */}
          {error && <p className={styles.error}>{error}</p>}

          {/* 로그인 버튼 */}
          <button
            className={styles.loginButton}
            onClick={onClickButtonLogin}
            disabled={isLoading}
          >
            <img
              src={loginImg}
              alt="loginImg"
              style={{ marginRight: "7px", width: "20px", height: "18px" }}
            />
            {isLoading ? "로그인 중..." : "로그인"}
          </button>

          {/* 회원가입 안내 */}
          <div style={{ display: "inline-block" }}>
            <span className={styles.notAccount} style={{ marginRight: "10px" }}>
              계정이 없으신가요?
            </span>
            <button className={styles.signUp} onClick={onClickHandleSignUp}>
              회원가입
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
