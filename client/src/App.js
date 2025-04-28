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
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/Longin" element={<LoginPage />} />
        <Route path="/Loged" element={<Loged />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/Signup" element={<SignupPage />} />
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
