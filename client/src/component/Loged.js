import styles from './Loged.module.css'


export default function Loged(){
    
    return(
        <div className={styles.LogedPageContainer}>
            <p className={styles.name}>{name}</p>
            <p className={styles.welcome}>welcome to photomary!</p>
        </div>
    )
}