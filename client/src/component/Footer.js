import styles from './Footer.module.css'

function Footer(){
    return(
        <div className={styles.footerContainer}>
            <div className={styles.footerLogo}>
                logo
            </div>
            <div className={styles.footerText}>
                PKNU-WAB 2025-WEB 6 TEAM
            </div>
        </div>
    )
}

export default Footer