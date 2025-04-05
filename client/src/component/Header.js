import styles from './Header.module.css'
import { useNavigate } from 'react-router-dom';

function Header() {
    const nav = useNavigate();
    const onclickHandle = (event) => {
        nav(event.target.value);
    };

    return (
        <div className={styles.headerContainer}>
            <div className={styles.headerContainerLeft}>
                <div className={styles.headerTopLogo}>
                    logo
                </div>
                <button className={styles.home}
                        onClick={onclickHandle}
                        value='/'>
                    home
                </button>
                <div className={styles.memoryContainer}>
                    <button className={styles.myMemory}
                            onClick={onclickHandle}
                            value='/myMemory'>
                        my memory
                    </button>
                    <button className={styles.ourMemory}
                            onClick={onclickHandle}
                            value='/ourMemory'>
                        our memory
                    </button>
                    <button className={styles.weeklyMemory}
                            onClick={onclickHandle}
                            value='/weeklyMemory'>
                        every memory
                    </button>
                </div>
            </div>
            <div className={styles.headerContainerRight}>
                <button className={styles.signIn}
                        onClick={onclickHandle}
                        value='/signIn'>
                    sign in
                </button>
                <button className={styles.signUp}
                        onClick={onclickHandle}
                        value='/signUp'>
                    sign up
                </button>
            </div>
        </div>
    );
}

export default Header