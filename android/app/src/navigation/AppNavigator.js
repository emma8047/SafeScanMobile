import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import MainStack from './MainStack';
import AuthStack from './AuthStack';
import { useAuth } from '../context/AuthContext';

export default function AppNavigator() {
  const { user } = useAuth();

  return (
    <NavigationContainer>
      {user ? <MainStack /> : <AuthStack />}
    </NavigationContainer>
  );
}