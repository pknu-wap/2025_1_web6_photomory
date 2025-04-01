import styles from './Header.module.css'

function Header(){
    return(
        <div className={styles.headerContainer}>
            <div className={styles.headerContainerLeft}>
                <div className={styles.headerTopLogo}>
                    icon
                </div>
                <div className={styles.home}>
                    home
                </div>
                <div className={styles.memoryContainer}>
                    <div className={styles.myMemory}>
                        myMemory
                    </div>
                    <div className={styles.ourMemory}>
                        ourMemory
                    </div>
                    <div className={styles.everyMemory}>
                        everyMemory
                    </div>
                </div>
            </div>
            <div className={styles.headerContainerRight}>
                <div className={styles.signUp}>
                    signUp
                </div>
                <div className={styles.signIn}>
                    signIn
                </div>
            </div>
        </div>
    );
}

export default Header;