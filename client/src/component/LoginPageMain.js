import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./LoginPage.Main.module.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEnvelope,
  faLock,
  faRightToBracket,
} from "@fortawesome/free-solid-svg-icons";
import logo from "../assets/photomory_logo.svg";

// loginUser.js (API 요청 함수)
export async function loginUser(email, password, navigate) {
  try {
    const response = await fetch("http://3.38.237.115:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        useremail: email,
        password: password,
      }),
    });

    if (!response.ok) {
      throw new Error("이메일 또는 비밀번호가 잘못되었습니다.");
    }

    const data = await response.json();

    if (!data.accessToken) {
      alert("❌ accessToken이 없습니다!");
      return null;
    }

    localStorage.setItem("accessToken", data.accessToken);
    localStorage.setItem("refreshToken", data.refreshToken);
    console.log("✅ accessToken:", localStorage.getItem("accessToken"));
    console.log("✅ refreshToken:", localStorage.getItem("refreshToken"));

    navigate("/Loged", {
      state: {
        name: data.userName, // "박진오"
        email: data.userEmail, // "jinoh1030@naver.com"
        password: password, // 사용자가 입력한 pw
      },
    });
    return {
      email: data.userEmail,
      name: data.userName,
    };
  } catch (error) {
    console.error("로그인 에러:", error.message);
    alert("로그인에 실패했습니다. 다시 시도해주세요.");
    return null;
  }
}

export default function LoginPageMain() {
  const [email, setEmail] = useState("");
  const [pw, setPw] = useState("");
  const [error, setError] = useState();
  const [isLoading, setIsLoading] = useState(false);
  const [isLogged, setIsLogged] = useState(false);
  const navigate = useNavigate();
  const focusEmailRef = useRef();
  const focusPwRef = useRef();

  const onChangeHandleEmail = (e) => {
    setEmail(e.target.value);
    setError("");
  };
  const onChangeHandlePw = (e) => {
    setPw(e.target.value);
    setError("");
  };

  const onClickButtonLogin = async () => {
    setIsLoading(true);
    try {
      if (email === "") {
        focusEmailRef.current.focus();
        setError("이메일을 입력해주세요.");
        return;
      } else if (pw === "") {
        focusPwRef.current.focus();
        setError("비밀번호를 입력해주세요.");
        return;
      }

      const user = await loginUser(email, pw, navigate);
      if (user) {
        setIsLogged(true);
        // 사용자 정보를 상태로 저장하고 싶다면 여기서 추가 가능
      } else {
        setEmail("");
        setPw("");
        setError("이메일 또는 비밀번호가 잘못되었습니다.");
        focusEmailRef.current.focus();
      }
    } catch (error) {
      setEmail("");
      setPw("");
      console.error("An error occurred during login");
      setError("로그인 중 오류가 발생했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  const onClickHandleSignUp = () => {
    navigate("/signUp");
  };

  return (
    <div className={styles.loginPageMainContainer}>
      <img src={logo} className={styles.logoImage} alt="Photomy" />
      <div className={styles.loginContainer}>
        <p className={styles.loginText}>로그인</p>
        <div className={styles.emailContainer}>
          <span className={styles.emailText}>email</span>
          <FontAwesomeIcon icon={faEnvelope} className={styles.emailIcon} />
          <input
            className={styles.emailInput}
            placeholder="     이메일을 입력하세요."
            onChange={onChangeHandleEmail}
            value={email}
            disabled={isLoading}
            ref={focusEmailRef}
          />
        </div>
        <div className={styles.pwContainer}>
          <span className={styles.pwText}>password</span>
          <FontAwesomeIcon icon={faLock} className={styles.pwIcon} />
          <input
            className={styles.pwInput}
            type="password"
            placeholder="     비밀번호를 입력하세요."
            onChange={onChangeHandlePw}
            value={pw}
            disabled={isLoading}
            ref={focusPwRef}
          />
        </div>
        {error && <p className={styles.error}>{error}</p>}
        <button
          className={styles.loginButton}
          onClick={onClickButtonLogin}
          disabled={isLoading}
        >
          <FontAwesomeIcon icon={faRightToBracket} />
          {isLoading ? "로딩 중..." : "LOGIN"}
        </button>
        <span className={styles.notAccount}>계정이 없으신가요?</span>
        <button className={styles.signUp} onClick={onClickHandleSignUp}>
          회원가입
        </button>
      </div>
    </div>
  );
}
