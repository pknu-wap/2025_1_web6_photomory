import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AuthProvider from "./contexts/AuthContext";
import MainPage from "./page/MainPage.js";
import LoginPage from "./page/LoginPage.js";
import Loged from "./component/login/Loged.js";
import ProfilePage from "./page/ProfilePage.js";
import OurAlbumPage from "./page/OurAlbumPage";
import OurAlbumDetailPage from "./page/OurAlbumDetailPage";
import GroupEditPage from "../src/page/GroupEditPage";
import SignupPage from "./page/SignupPage";
import MyAlbumPage from "./page/MyAlbumPage.js";
import MyAlbumDetailPage from "./page/MyAlbumDetailPage.js";
import SignupConfirmPage from "./page/SignupConfirmPage.js";
import EveryMemoryPage from "./page/EveryMemoryPage.js";

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<MainPage />} />
          <Route path="/Login" element={<LoginPage />} />
          <Route path="/Loged" element={<Loged />} />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/Signup" element={<SignupPage />} />
          <Route path="/Signup/Confirm" element={<SignupConfirmPage />} />
          <Route path="/my-album" element={<MyAlbumPage />} />
          <Route path="/my-album/:albumId" element={<MyAlbumDetailPage />} />
          <Route path="/our-album" element={<OurAlbumPage />} />
          <Route
            path="/our-album/:groupId/:albumId"
            element={<OurAlbumDetailPage />}
          />
          <Route
            path="/our-album/:groupId/groupEdit"
            element={<GroupEditPage />}
          />
          <Route path="/everyMemory" element={<EveryMemoryPage />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
