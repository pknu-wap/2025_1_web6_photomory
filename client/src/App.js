import LoginPage from "./page/LoginPage.js";
import Loged from './component/Loged.js'
import { Route, Routes } from "react-router-dom";

function App() {
  return (
    <Routes>
      <Route path="/" element={<LoginPage/>}></Route>
      <Route path="/Loged" element={<Loged/>}></Route>
    </Routes>
  );
}

export default App;
