import Header from "../component/common/Header";
import ProfileMain from "../component/Profile.Main.js";
import Footer from "../component/Footer.js";
import styles from "./Profile.Page.module.css";

function ProfilePage() {
  return (
    <div className={styles.pageContainer}>
      <Header />
      <ProfileMain />
      <Footer className={styles.footer} />
    </div>
  );
}

export default ProfilePage;
