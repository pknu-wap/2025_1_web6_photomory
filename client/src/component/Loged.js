import styles from './Loged.module.css'
import { useLocation } from 'react-router-dom'

export default function Loged(){
    const location= useLocation();
    const {name, id, pw} = location.state || {};

    return(
        <div className={styles.forFlex}>
            <div className={styles.logedPageContainer}>
                <p className={styles.name}>{name+','}</p>
                <p className={styles.welcome}>welcome to photomory!</p>
                <p className={styles.LogedId}>id: {id}</p> {/* 아이디는 이메일과 동일*/}
                <p className={styles.LogedPw}>password: {pw}</p>
                <button></button>{/*집 가기*/}
            </div>
        </div>
    )
}