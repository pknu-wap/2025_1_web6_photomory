import { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import MainPage from "./page/MainPage.js";
import LoginPage from "./page/LoginPage.js";
import Loged from "./component/Loged.js";
import ProfilePage from "./page/ProfilePage.js";
import OurAlbumPageTest from "./page/OurAlbumPageTest";
import OurAlbumDetailPageTest from "./page/OurAlbumDetailPage";
import GroupEditPage from "../src/page/GroupEditPage";
import SignupPage from "./page/SignupPage";
import MyAlbumPage from "./page/MyAlbumPage.js";
import MyAlbumDetailPage from "./page/MyAlbumDetailPage.js";
import SignupConfirmPage from "./page/SignupConfirmPage.js";
function App() {
  const [isLogged, setIsLogged] = useState(false);
  const [name, setName] = useState("");

  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route
          path="/Login"
          element={<LoginPage isLogged={isLogged} setIsLogged={setIsLogged} name={name} setName={setName} />}
        />
        <Route path="/Loged" element={<Loged />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/Signup" element={<SignupPage />} />
        <Route path="/Signup/Confirm" element={<SignupConfirmPage />} />
        <Route path="/my-album" element={<MyAlbumPage />} />
        <Route path="/my-album/:albumId" element={<MyAlbumDetailPage />} />
        <Route path="/our-album" element={<OurAlbumPageTest />} />
        <Route path="/our-album/groupEdit" element={<GroupEditPage />} />
        <Route
          path="/our-album/:albumId"
          element={<OurAlbumDetailPageTest />}
        />
      </Routes>
    </Router>
  );
}

export default App;
