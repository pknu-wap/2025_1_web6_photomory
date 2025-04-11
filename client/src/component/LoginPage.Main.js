import styles from './LoginPage.Main.module.css'
import GetUserLogin from '../api/GetUserLogin.js'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEnvelope, faLock, faRightToBracket } from '@fortawesome/free-solid-svg-icons'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'


export default function LoginPageMain() {
    const [email, setEmail] = useState('')
    const [pw, setPw] = useState('')
    const navigate = useNavigate();

    const onChangeHandle = (e) => {
        if (e.target.className === 'emailInput') {
            setEmail(e.target.value)
        }
        if (e.target.className === 'pwInput') {
            setPw(e.target.value)
        }
    }

    const onClickButtonLogin = () => {
        const user = GetUserLogin.find((u) => u.email === email && u.password === pw);
    
        
        if (user) {
            navigate('/Loged', {
            state: {
            name: user.name
            },
        });
        } 
        else {
            setEmail('')
            setPw('')
        }
    };

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
                        onChange={onChangeHandle}
                        value={email}></input>
                    </div>
                    <div className={styles.pwContainer}>
                        <span className={styles.pwText}>
                            password
                            <FontAwesomeIcon icon={faLock}
                            className={styles.pwIcon}/>
                        </span>
                        <input className={styles.pwInput} 
                        placeholder="비밀번호를 입력해줘요!"
                        onChange={onChangeHandle}
                        value={pw}></input>
                    </div>
                    <button className={styles.loginButton}
                    onClick={onClickButtonLogin}>
                        <FontAwesomeIcon icon={faRightToBracket} />
                        LOGIN
                    </button>
                        <apsn className={styles.notAccount}>계정이 없으신가요?</apsn>
                        <button className={styles.signUp}>회원가입</button>
                    
                </div>
            </div>
        </>
    )
}