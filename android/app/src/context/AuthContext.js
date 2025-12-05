import React, { createContext, useContext, useState, useEffect } from 'react';
import auth from '@react-native-firebase/auth';

const AuthContext = createContext({});

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const unsubscribe = auth().onAuthStateChanged(setUser);
    return unsubscribe;
  }, []);

  const login = (email, password) => auth().signInWithEmailAndPassword(email, password);
  const register = (email, password) => auth().createUserWithEmailAndPassword(email, password);
  const logout = () => auth().signOut();

  return (
    <AuthContext.Provider value={{ user, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);