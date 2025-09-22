import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import HomeScreen from '../Screens/Home/HomeScreen';
import FaceDetector from '../Screens/SignUp-In/FaceDetector.tsx';
import {useAuth} from '../Contexts/AuthContext.tsx';

const Stack = createNativeStackNavigator();


const HomeStack = () => {
    const {isAuthenticated} = useAuth();

    return (
    <Stack.Navigator initialRouteName="HomeMain">
      <Stack.Screen
        name="HomeMain"
        component={HomeScreen}
        options={{ headerShown: false }}
      />
        <Stack.Screen
            name="RiconoscimentoFacciale"
            options={{ title: 'Face Detector', headerShown: true }}>
            {() => <FaceDetector isAuthenticated={isAuthenticated} />}
        </Stack.Screen>
    </Stack.Navigator>
  );
};

export default HomeStack;