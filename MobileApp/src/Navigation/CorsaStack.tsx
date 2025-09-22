import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import ScanQR from '../Screens/QR/ScanQR'; 
import CorsaStart from '../Screens/QR/CorsaStart';


const Stack = createNativeStackNavigator();

const CorsaStack = () => {
  return (
    <Stack.Navigator initialRouteName="QRMain">
        <Stack.Screen
            name="QRMain"
            component={ScanQR}
            options={{ headerShown: false }}
        />
        <Stack.Screen
            name="CorsaStartScreen"
            component={CorsaStart}
            options={{ title: 'Inizio Corsa' }}
        />
  </Stack.Navigator>
  );
};

export default CorsaStack;