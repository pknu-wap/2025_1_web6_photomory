import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";
import styles from "./LoginPage.Main.module.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faEnvelope,
  faLock,
  faRightToBracket,
} from "@fortawesome/free-solid-svg-icons";
import logo from "../assets/photomory_logo.svg";

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
    localStorage.setItem("userName", data.userName);
    localStorage.setItem("userEmail", data.userEmail);

    navigate("/Loged", {
      state: {
        name: data.userName, // "박진오"
        email: data.userEmail, // "jinoh1030@naver.com"
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
          name: user.userName,
          id: user.userEmail, //id는 이메일과 동일
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
            <span className={styles.emailText}>email</span>
            <FontAwesomeIcon icon={faEnvelope} className={styles.emailIcon} />
            <input
              className={styles.emailInput}
              placeholder="     이메일을 입력하세요."
              onChange={onChangeHandleEmail}
              value={email}
              disabled={isLoading}
              ref={focusEmailRef}
              onFocus={() => {}} // TODO: 원하는 포커스 동작 추가
            />
          </div>

          {/* 비밀번호 입력 */}
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

          {/* 에러 메시지 출력 */}
          {error && <p className={styles.error}>{error}</p>}

          {/* 로그인 버튼 */}
          <button
            className={styles.loginButton}
            onClick={onClickButtonLogin}
            disabled={isLoading}
          >
            <FontAwesomeIcon icon={faRightToBracket} />
            {isLoading ? "로그인 중..." : "LOGIN"}
          </button>

          {/* 회원가입 안내 */}
          <span className={styles.notAccount}>계정이 없으신가요?</span>
          <button className={styles.signUp} onClick={onClickHandleSignUp}>
            회원가입
          </button>
        </div>
      </div>
    </>
  );
}
