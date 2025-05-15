import Header from "../component/common/Header";
import ProfileMain from "../component/profile/Profile.Main.js";
import Footer from "../component/common/Footer";
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
