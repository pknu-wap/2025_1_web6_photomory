import styles from './LoginPage.Main.module.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEnvelope, faLock, faRightToBracket } from '@fortawesome/free-solid-svg-icons'
import { useState } from 'react'


export default function LoginPageMain() {
    const [email, setEmail] = useState('')
    const [pw, setPw] = useState('')

    const onChangeHandle = (e) => {
        if (e.target.className === 'emailInput') {
            setEmail(e.target.value)
        }
        if (e.target.className === 'pwInput') {
            setPw(e.target.value)
        }
    }

    return(
        <>
            <div className={styles.loginPageMainContainer}>
                <img className={styles.logoImage} alt="logoImage"></img>

                <div className={styles.loginContainer}>
                    <p className={styles.loginText}>Login</p>
                    <div className={styles.emailContainer}>
                        <span className={styles.emailText}>
                            email
                            <FontAwesomeIcon icon={faEnvelope}
                            className={styles.emailIicon}/>
                        </span>
                        <input className={styles.emailInput} 
                        placeholder="이메일을 입력해줘요!"
                        onChange={onChangeHandle}></input>
                    </div>
                    <div className={styles.pwContainer}>
                        <span className={styles.pwText}>
                            password
                            <FontAwesomeIcon icon={faLock}
                            className={styles.pwIcon}/>
                        </span>
                        <input className={styles.pwInput} 
                        placeholder="비밀번호를 입력해줘요!"
                        onChange={onChangeHandle}></input>
                    </div>
                    <button className={styles.loginButton}>
                        <FontAwesomeIcon icon={faRightToBracket} />
                        LOGIN
                    </button> {/*기능 추가하기*/}
                        계정이 없으신가요?
                        <button className={styles.signUp}>회원가입</button>
                    
                </div>
            </div>
        </>
    )
}