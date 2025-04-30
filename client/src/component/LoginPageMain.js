import styles from './LoginPage.Main.module.css'
import logo from "../assets/photomory_logo.svg"
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEnvelope, faLock, faRightToBracket } from '@fortawesome/free-solid-svg-icons'
import { useState, useRef } from 'react'
import { useNavigate } from 'react-router-dom' 


async function loginUser(email,password) {
    try {
    const response = await fetch('http://3.38.237.115:8080/api/auth/login', {
        method: 'POST',
        headers: {
        'Content-Type': 'application/json',
        },
        body: JSON.stringify({
        useremail: email,
        password: password,
        }),
    });

    if (!response.ok) { //응답에 따른
        throw new Error('이메일 또는 비밀번호가 잘못되었습니다.');
    }

    const data = await response.json(); //서버로부터 받은 json 응답 처리리
    const accessToken = data.accessToken;
    const refreshToken = data.refreshToken; 
    const user = data.user; //user로 주나..?
    if (!accessToken || !refreshToken || !user) {
        throw new Error('응답에 토큰 또는 사용자 정보가 없습니다.')
    }

      // 토큰 저장
    localStorage.setItem('token', refreshToken);
    console.log('로그인 성공, 토큰 저장 완료:', refreshToken);
    return user; // 사용자 정보 반환
    
} catch (error) {
    console.error('로그인 에러:', error.message);
    alert('로그인에 실해하였습니다. 다시 시도해주세요.'); 
    }
}

export default function LoginPageMain() {
    const [ email, setEmail] = useState('')
    const [ pw, setPw] = useState('')
    const [ error, setError] = useState();
    const [ isLoading , setIsLoading] = useState(false)
    const [isLogged, setIsLogged] = useState(false)
    const navigate = useNavigate();
    const focusEmailRef = useRef();
    const focusPwRef = useRef();

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
            if (email==="") {
                focusEmailRef.current.focus();
                setError('이메일을 입력해주세요.')
                return;
            }else if (pw==="") {
                focusPwRef.current.focus();
                setError('비밀번호를 입력해주세요.')
                return;
            }
            
            const user = await loginUser(email, pw);
            if (user) { //로그인 성공
                setIsLogged(true)
                navigate('/Loged', {
                state: {
                name: user.userName,
                id: user.userEmail, //id는 이메일과 동일
                

                }
                //여기에 내 정보 제이슨=user로 하기 지금 그 파일 추가하면 머지하다가 오류남
            });
            } else { //로그인 실패
                setEmail('')
                setPw('')
                setError('이메일 또는 비밀번호가 잘못되었습니다.')
                focusEmailRef.current.focus();
            };
        } catch (error) {
            setEmail('')
            setPw('')
            console.error('An error occurred during login');
            setError('로그인 중 오류가 발생했습니다.');
        } finally {
            setIsLoading(false); //로딩 종료
        }
    };

    const onClickHandleSignUp=()=>{
        navigate('/signUp')
    }
    return(
        <>
            <div className={styles.loginPageMainContainer}>
                <img src={logo} className={styles.logoImage} alt="Photomy"></img>
                <div className={styles.loginContainer}>
                    <p className={styles.loginText}>로그인</p>
                    <div className={styles.emailContainer}>
                        <span className={styles.emailText}>
                            email
                        </span>
                        {/* <FontAwesomeIcon icon={faEnvelope}
                        className={styles.emailIcon}/>  */}
                        <input className={styles.emailInput} 
                        placeholder="     이메일을 입력하세요."
                        onChange={onChangeHandleEmail}
                        value={email}
                        disabled={isLoading}
                        ref={focusEmailRef}></input>
                    </div>
                    <div className={styles.pwContainer}>
                        <span className={styles.pwText}>
                            password
                        </span>
                        {/* <FontAwesomeIcon icon={faLock} className={styles.pwIcon}/>  */}
                        <input className={styles.pwInput}
                        type='password'
                        placeholder="     비밀번호를 입력하세요."
                        onChange={onChangeHandlePw}
                        value={pw}
                        disabled={isLoading}
                        ref={focusPwRef}></input>
                    </div>
                    { error&& <p className={styles.error}>{error}</p>}
                    <button className={styles.loginButton}
                    onClick={onClickButtonLogin}
                    disabled={isLoading}>
                        <FontAwesomeIcon icon={faRightToBracket} />
                        {isLoading? isLoading : 'LOGIN'}
                    </button>
                        <span className={styles.notAccount}>계정이 없으신가요?</span>
                        <button className={styles.signUp}
                        onClick={onClickHandleSignUp}>회원가입</button>
                </div>
            </div>
        </>
    )
}