import styles from './LoginPage.Main.module.css'
import GetUserLogin from '../api/GetUserLogin.js'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEnvelope, faLock, faRightToBracket } from '@fortawesome/free-solid-svg-icons'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom' 
포커스 주기, 로그인 완료 창 마저 끝내기

export default function LoginPageMain() {
    const [ email, setEmail] = useState('')
    const [ pw, setPw] = useState('')
    const [ error, setError] = useState();
    const [ isLoading , setIsLoading] = useState(false)
    const navigate = useNavigate();

    const onChangeHandleEmail = (e) => {
        setEmail(e.target.value)
        setError('')
    }
    const onChangeHandlePw = (e) => {
        setPw(e.target.value)
        setError('')
    }

    const onClickButtonLogin = async () => {
        setIsLoading(true);
        try{
            const userLogin = await GetUserLogin()
            const user = userLogin.find((u) => u.email === email && u.password === pw);
            if (user) { //로그인 성공
                navigate('/Loged', {
                state: {
                name: user.name,
                id: user.id,
                pw: user.password
                }
            });
            } 
            else { //로그인 실패
                setEmail('')
                setPw('')
                setError('이메일 또는 비밀번호가 잘못되었어요!')
            }
        } catch (error) {
            console.error('An error occurred during login');
            setError('로그인 중 오류가 발생했습니다.');
        } finally {
            setIsLoading(false); //로딩 종료
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
                        onChange={onChangeHandleEmail}
                        value={email}
                        disabled={isLoading}></input>
                    </div>
                    <div className={styles.pwContainer}>
                        <span className={styles.pwText}>
                            password
                            <FontAwesomeIcon icon={faLock}
                            className={styles.pwIcon}/>
                        </span>
                        <input className={styles.pwInput}
                        type='password'
                        placeholder="비밀번호를 입력해줘요!"
                        onChange={onChangeHandlePw}
                        value={pw}
                        disabled={isLoading}></input>
                    </div>
                    { error&& <p className={styles.error}>{error}</p>}
                    <button className={styles.loginButton}
                    onClick={onClickButtonLogin}
                    disabled={isLoading}>
                        <FontAwesomeIcon icon={faRightToBracket} />
                        {isLoading? isLoading: 'LOGIN'}
                    </button>
                        <span className={styles.notAccount}>계정이 없으신가요?</span>
                        <button className={styles.signUp}>회원가입</button>
                    
                </div>
            </div>
        </>
    )
}