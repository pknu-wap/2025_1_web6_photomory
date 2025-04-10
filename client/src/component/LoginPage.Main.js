import styles from './LoginPage.Main.module.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEnvelope, faLock, faRightToBracket } from '@fortawesome/free-solid-svg-icons'


export default function LoginPageMain() {
    
    return(
        <>
            <div className={styles.loginPageMainContainer}>
                <img className={styles.logoImage} alt="logoImage"></img>

                <div className={styles.loginContainer}>
                    <p className={styles.loginText}>Login</p>
                    <div className={styles.emailContainer}>
                        <span className={styles.emailText}>
                            email
                            <FontAwesomeIcon icon={faEnvelope}/>
                        </span>
                        <input className={styles.emailInput} 
                        placeholder="이메일을 입력해줘요!"></input>
                    </div>
                    <div className={styles.pwContainer}>
                        <span className={styles.pwText}>
                            password
                            <FontAwesomeIcon icon={faLock}
                            className={styles.icon}/>
                        </span>
                        <input className={styles.pwInput} 
                        placeholder="비밀번호를 입력해줘요!"></input>
                    </div>
                    <button className={styles.loginButton}>
                        <FontAwesomeIcon icon={faRightToBracket} />
                        LOGIN
                    </button>
                    <p className={styles.forgotPw}>비밀번호를 잊으셨나요?</p>
                    <p className={styles.or}>또는</p>
                    <span className={styles.notAccount}>
                        계정이 없으신가요?
                        <sapn className={styles.signUp}></sapn>
                    </span>
                </div>
            </div>
        </>
    )
}