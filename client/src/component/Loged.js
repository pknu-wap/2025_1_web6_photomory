import styles from './Loged.module.css'


export default function Loged({name, id, pw}){
    
    return(
        <div className={styles.LogedPageContainer}>
            <p className={styles.name}>{name+','}</p>
            <p className={styles.welcome}>welcome to photomary!</p>
            <p className={styles.LogedId}>id: {id}</p>
            <p className={styles.LogedPw}>password: {pw}</p>
        </div>
    )
}