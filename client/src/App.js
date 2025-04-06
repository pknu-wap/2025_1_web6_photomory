import { BrowserRouter } from 'react-router-dom';
import { useState, createContext } from 'react';
import Profile from "./page/Profile.Page";
export const context=createContext();

function App() {
  return (
    <BrowserRouter>
      <context.Provider>
        <Profile></Profile>
      </context.Provider>
    </BrowserRouter>
  );
}

export default App;
