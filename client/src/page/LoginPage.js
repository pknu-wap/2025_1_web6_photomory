import styles from "./LoginPage.module.css";
import Header from "../component/Header";
import Footer from "../component/Footer";
import LoginPageMain from "../component/LoginPageMain";


export default function LoginPage({
  isLogged,
  setIsLogged,
  name, 
  setName}) {
  

  return (
    <>
      <div className={styles.pageContainer}>
        <Header isLogged={isLogged} name={name}/>
        <LoginPageMain setIsLogged={setIsLogged} setName={setName}/>
        <Footer />
      </div>
    </>
  );
}
