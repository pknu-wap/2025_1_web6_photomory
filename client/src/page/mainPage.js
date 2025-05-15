import Header from "../component/common/Header";
import MainPageMain from "../component/MainPage.Main";
import Footer from "../component/Footer";
import "./MainPage.css";

function MainPage() {
  return (
    <div className="pageContainer">
      <Header className="Header" />
      <MainPageMain className="MainPageMain" />
      <Footer className="Footer" />
    </div>
  );
}

export default MainPage;
