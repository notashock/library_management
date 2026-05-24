import {
  createContext,
  useContext,
  useEffect,
  useState,
} from "react";



const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  useEffect(() => {

  const token =
    localStorage.getItem("token");

  const role =
    localStorage.getItem("role");

  const memberId =
    localStorage.getItem("memberId");

  if (token && role) {

    setUser({
      role,
      memberId,
    });
  }

}, []);
 const login = (token, role, memberId) => {

  localStorage.setItem(
    "token",
    token
  );

  localStorage.setItem(
    "role",
    role
  );

  if (memberId) {
    localStorage.setItem(
      "memberId",
      memberId
    );
  }

  setUser({
    role,
    memberId,
  });
};

  const logout = () => {
    localStorage.removeItem("token");

localStorage.removeItem("role");
localStorage.removeItem("memberId");

    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};