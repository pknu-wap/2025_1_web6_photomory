import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Profile from "./page/Profile.Page";
import OurAlbumPage from "./page/OurAlbumPage";
import OurAlbumPageTest from "./page/OurAlbumPageTest";
import OurAlbumDetailPageTest from "./page/OurAlbumDetailPage";
import GroupEditPage from "../src/page/GroupEditPage";
import SignupPage from "./page/SignupPage";
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/profile" element={<profile />}>
        <Route path="/Signup" element={<SignupPage />} />
        <Route path="/our-album" element={<OurAlbumPage />} />
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
