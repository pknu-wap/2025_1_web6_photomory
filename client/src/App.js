import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import AuthProvider from "./contexts/AuthContext";
import { RandomIndexProvider } from "./contexts/RandomIndexContext";
import MainPage from "./page/MainPage.js";
import LoginPage from "./page/LoginPage.js";
import Loged from "./component/login/Loged.js";
import ProfilePage from "./page/ProfilePage.js";
import NotificationPage from "./page/NotificationPage.js";
import OurAlbumPage from "./page/OurAlbumPage";
import OurAlbumDetailPage from "./page/OurAlbumDetailPage";
import GroupEditPage from "../src/page/GroupEditPage";
import SignupPage from "./page/SignupPage";
import MyAlbumPage from "./page/MyAlbumPage.js";
import MyAlbumDetailPage from "./page/MyAlbumDetailPage.js";
import SignupConfirmPage from "./page/SignupConfirmPage.js";
import EveryMemoryPage from "./page/EveryMemoryPage.js";
import ApiTestPage from "./page/ApiTestPage.js";

function App() {
  return (
    <AuthProvider>
      <RandomIndexProvider>
        <Router>
          <Routes>
            <Route path="/" element={<MainPage />} />
            <Route path="/Login" element={<LoginPage />} />
            <Route path="/Loged" element={<Loged />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/Notification" element={<NotificationPage />} />
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
            <Route path="/ApiTestPage" element={<ApiTestPage />} />
          </Routes>
        </Router>
      </RandomIndexProvider>
    </AuthProvider>
  );
}

export default App;
