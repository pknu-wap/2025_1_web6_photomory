import Header from "../component/Header.js";
import ProfileMain from "../component/Profile.Main.js";
import Footer from "../component/Footer.js";
import styles from "./Profile.Page.module.css";

function ProfilePage() {
  return (
    <div className={styles.pageContainer}>
      <Header />
      <ProfileMain />
      <Footer />
    </div>
  );
}

export default ProfilePage;
