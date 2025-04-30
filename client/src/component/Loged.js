import styles from "./Loged.module.css";
import { useLocation, useNavigate } from "react-router-dom";

export default function Loged() {
  const location = useLocation();
  const { name, email, password } = location.state || {};
  const navigete = useNavigate();

  const onClickHandle = () => {
    navigete("/");
  };
  return (
    <div className={styles.forFlex}>
      <div className={styles.logedPageContainer}>
        <p className={styles.name}>{name + "님,"}</p>
        <p className={styles.welcome}>포토모리에 온 걸 환영해요!</p>
        <p className={styles.LogedId}>email: {email}</p>{" "}
        {/* 아이디는 이메일과 동일*/}
        <p className={styles.LogedPw}>password: {password}</p>
        <div className={styles.forFlex2}>
          <button onClick={onClickHandle} className={styles.homeButton}>
            home
          </button>
        </div>
      </div>
    </div>
  );
}
