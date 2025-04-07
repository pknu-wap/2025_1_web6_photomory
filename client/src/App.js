import { BrowserRouter } from 'react-router-dom';
import { createContext } from 'react';
import Profile from "./page/Profile.Page";
export const context = createContext();
const friend = [
  {
    from_user_id: 1,
    user_name: "권동",
    are_we_friend: true,
    user_field: '사진 찍기'
  }
]
const users = [
  {
    user_id: 1,
    user_name: "권동",
    are_we_friend: null
  }
]

function App() {
  return (
    <BrowserRouter>
      <context.Provider value={{ friend, users }}>
        <Profile></Profile>
      </context.Provider>
    </BrowserRouter>
  );
}

export default App;
