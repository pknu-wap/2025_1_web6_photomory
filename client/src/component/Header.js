import styles from './Header.module.css'
import { useNavigate } from 'react-router-dom';
import logo from '../assets/photomory_logo.svg';
import { faGear, faBell } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';


function Header({isLogged, name }){ //로그인에서 받기
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
                    {isLogged ? (
                        <>
                            <div className={styles.welcomeButton}>
                                <img src='#' alt='#' 
                                className={styles.image}/>
                                <span className={styles.welcome}>{name}님, 반갑습니다!</span>
                            </div>
                            <button className={styles.myInfoButton}
                            onClick={onclickHandle}
                            value='/profile'>
                                <FontAwesomeIcon icon={faGear} className={styles.gearIcon}/>
                            </button>
                            <FontAwesomeIcon icon={faBell} 
                            className={styles.bellIcon} /> {/*알람 추후*/}
                        </>
                        ) : (
                        <>
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
                        </>
                    )}
                </div>
            </div>
        </div>
    );

export default Header;