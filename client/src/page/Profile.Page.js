import Header from '../component/Header.js'
import ProfileMain from '../component/Profile.Main.js';
import Footer from '../component/Footer.js';
import styles from './Profile.Page.module.css'

function Profile(){

    return(
        <div className={styles.pageContainer}>
            <Header></Header>
            <ProfileMain></ProfileMain>
            <Footer></Footer>
        </div>
    );
};

export default Profile