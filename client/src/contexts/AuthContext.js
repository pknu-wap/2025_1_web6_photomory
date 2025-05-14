import { createContext, useContext, useState, useEffect } from "react";

//로그인 관련 상태 공유할 컨텍스트
const AuthContext = createContext();

export default function AuthProvider({ children }) {
  const [isLogged, setIsLogged] = useState(false); //로그인 여부 상태
  const [name, setName] = useState(""); //사용자 이름 상태

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    const savedName = localStorage.getItem("userName");

    //token이 있을 시 로그인 상태 정보 유지
    if (token) {
      setIsLogged(true);
      if (savedName) setName(savedName);
    }
  }, []);

  return (
    <AuthContext.Provider value={{ isLogged, setIsLogged, name, setName }}>
      {children}
    </AuthContext.Provider>
  );
}

// 로그인 정보를 가져올 수 있도록 하는 커스텀 훅
export function useAuth() {
  return useContext(AuthContext);
}
