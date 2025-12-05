import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import HomeScreen from '../screens/HomeScreen';
import ScanScreen from '../screens/ScanScreen';
import TaskManagerScreen from '../screens/ProfileScreen';
import ReportsScreen from '../screens/ReportScreen';

const Tab = createBottomTabNavigator();

export default function MainStack() {
  return (
    <Tab.Navigator screenOptions={{ headerShown: false }}>
      <Tab.Screen name="Dashboard" component={HomeScreen} />
      <Tab.Screen name="Investigate" component={ScanScreen} />
      <Tab.Screen name="TaskManager" component={TaskManagerScreen} />
      <Tab.Screen name="Reports" component={ReportsScreen} />
    </Tab.Navigator>
  );
}