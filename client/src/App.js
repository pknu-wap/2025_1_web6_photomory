import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import MainPage from "./page/MainPage.js";
import LoginPage from "./page/LoginPage.js";
import Loged from "./component/Loged.js";
import ProfilePage from "./page/ProfilePage.js";
import OurAlbumPage from "./page/OurAlbumPage";
import OurAlbumPageTest from "./page/OurAlbumPageTest";
import OurAlbumDetailPageTest from "./page/OurAlbumDetailPage";
import GroupEditPage from "../src/page/GroupEditPage";
import SignupPage from "./page/SignupPage";
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/Login" element={<LoginPage />} />
        <Route path="/Loged" element={<Loged />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/Signup" element={<SignupPage />} />
        <Route path="/ourMemory" element={<OurAlbumPage />} />
        <Route path="/our-albumTest" element={<OurAlbumPageTest />} />
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
