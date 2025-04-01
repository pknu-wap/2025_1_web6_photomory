import Header from "../component/Header";
import MainPageMain from "../component/MainPage.Main";
import Footer from "../component/Footer";
import './MainPage.css'

function MainPage(){
    return(
        <div className="pageContainer">
            <Header className="Header"></Header>
            <MainPageMain className="MainPageMain"></MainPageMain>
            <Footer className="Footer"></Footer>
        </div>
    )
}

export default MainPage;