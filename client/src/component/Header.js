import styles from './Header.module.css'
import { useNavigate } from 'react-router-dom';
import logo from '../assets/photomory_logo.svg';

function Header() {
  const nav = useNavigate();
  const onclickHandle = (event) => {
    nav(event.target.value);
  };
    return(
        <div className={styles.headerContainer}>
            <div className={styles.headerContainer2}>
                <div className={styles.headerContainerLeft}>
                <img src={logo} alt="Photomory Logo" className={styles.headerTopLogo} />
                    <button className={styles.home}
                    onClick={onclickHandle}
                    value='/'>
                        홈
                    </button>
                    <div className={styles.memoryContainer}>
                        <button className={styles.myMemory}
                        onClick={onclickHandle}
                        value='/my-album'>
                            나만의 추억
                        </button>
                        <button className={styles.ourMemory}
                        onClick={onclickHandle}
                        value='/our-album'>
                            우리의 추억
                        </button>
                        <button className={styles.everyMemory}
                        onClick={onclickHandle}
                        value='/everyMemory'>
                            모두의 추억
                        </button>
                    </div>
                </div>
                <div className={styles.headerContainerRight}>
                    <button className={styles.signIn}
                    onClick={onclickHandle}
                    value='/Login'>
                        로그인
                    </button>
                    <button className={styles.signUp}
                    onClick={onclickHandle}
                    value='/signUp'>
                        회원가입
                    </button>
                </div>
            </div>
        </div>
        <div className={styles.headerContainerRight}>
          <button
            className={styles.signIn}
            onClick={onclickHandle}
            value="/signIn"
          >
            sign in
          </button>
          <button
            className={styles.signUp}
            onClick={onclickHandle}
            value="/signUp"
          >
            sign up
          </button>
        </div>
      </div>
    </div>
  );
}

export default Header;
