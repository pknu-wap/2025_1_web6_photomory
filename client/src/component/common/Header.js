import styles from "./Header.module.css";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";
import logo from "../../assets/photomory_logo.svg";
import { faGear, faBell } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import defaultProfileIcon from "../../assets/defaultProfileIcon.svg";

function Header() {
  //로그인 여부, 사용자 이름 받기
  const { isLogged, name } = useAuth();

  //로그인에서 받기
  const nav = useNavigate();
  const location = useLocation();

  //라우팅 경로 변경 헨들러
  const onclickHandle = (event) => {
    const path = event.currentTarget.dataset.path || event.currentTarget.value; // data-path 우선, 없으면 value
    nav(path);
  };

  const isActive = (path) => {
    return location.pathname === path ? styles.active : "";
  }; //현재 경로가 헤더의 경로랑 같으으면 styles.active반환. 쉽게 말해 현재 경로랑 같으면 계속 active상태

  return (
    <div className={styles.headerContainer}>
      <div className={styles.headerContainer2}>
        <div className={styles.headerContainerLeft}>
          <img
            src={logo}
            alt="Photomory Logo"
            className={styles.headerTopLogo}
            onClick={onclickHandle}
            data-path="/"
          />
          <button
            className={`${styles.home} ${isActive("/")}`} //현재 경로가 /면 styles.active반환
            onClick={onclickHandle}
            value="/"
          >
            홈
          </button>
          <div className={styles.memoryContainer}>
            <button
              className={`${styles.myMemory} ${isActive("/my-album")}`}
              onClick={onclickHandle}
              value="/my-album"
            >
              나만의 추억
            </button>
            <button
              className={`${styles.ourMemory} ${isActive("/our-album")}`}
              onClick={onclickHandle}
              value="/our-album"
            >
              우리의 추억
            </button>
            <button
              className={`${styles.everyMemory} ${isActive("/everyMemory")}`}
              onClick={onclickHandle}
              value="/everyMemory"
            >
              모두의 추억
            </button>
          </div>
        </div>
        <div className={styles.headerContainerRight}>
          {isLogged ? (
            <>
              <div className={styles.welcomeButton}>
                <img
                  src={defaultProfileIcon}
                  alt="defaultProfileIcon"
                  className={styles.image}
                />
                <span className={styles.welcome}>{name}님, 반갑습니다!</span>
              </div>
              <button
                className={styles.myInfoButton}
                onClick={onclickHandle}
                value="/profile"
              >
                <FontAwesomeIcon icon={faGear} className={styles.gearIcon} />
              </button>
              {/*알람 추후*/}
              <button onClick={onclickHandle} value="/Notification">
                <FontAwesomeIcon icon={faBell} className={styles.bellIcon} />
              </button>
            </>
          ) : (
            <>
              <button
                className={styles.signIn}
                onClick={onclickHandle}
                value="/Login"
              >
                로그인
              </button>
              <button
                className={styles.signUp}
                onClick={onclickHandle}
                value="/signUp"
              >
                회원가입
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
export default Header;
