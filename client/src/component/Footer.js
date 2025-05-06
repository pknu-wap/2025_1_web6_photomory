import styles from './Footer.module.css'
import logo from '../assets/photomory_logo.svg';

function Footer() {
    return (
        <div className={styles.footerContainer}>
            <img src={logo} alt='PHOTOMORY' className={styles.logo}></img>
            <div className={styles.footerText}>
                PKNU-WAB 2025-WEB 6 TEAM
            </div>
        </div>
    )
}

export default Footer