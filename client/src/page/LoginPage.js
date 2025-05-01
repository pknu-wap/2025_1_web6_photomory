import styles from "./LoginPage.module.css";
import Header from "../component/Header";
import Footer from "../component/Footer";
import LoginPageMain from "../component/LoginPageMain";

export default function LoginPage() {
  return (
    <>
      <div className={styles.pageContainer}>
        <Header />
        <LoginPageMain />
        <Footer />
      </div>
    </>
  );
}
