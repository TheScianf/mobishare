import React from 'react';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import Profile from '../Screens/Profile/Profile';
import ProfileInfo from '../Screens/Profile/PersonalInfo';
import {useAuth} from '../Contexts/AuthContext.tsx';
import FaceDetector from '../Screens/SignUp-In/FaceDetector.tsx';

const Stack = createNativeStackNavigator();

const ProfileStack = () => {
  const {isAuthenticated} = useAuth();

  return (
    <Stack.Navigator initialRouteName="ProfileMain">
      <Stack.Screen
        name="ProfileMain"
        component={Profile}
        options={{headerShown: false}}
      />
      <Stack.Screen
        name="PersonalInfoScreen"
        options={{title: 'Informazioni Personali'}}>
        {() => <ProfileInfo isAuthenticated={isAuthenticated} />}
      </Stack.Screen>
        <Stack.Screen
            name="RiconoscimentoFacciale"
            options={{ title: 'Face Detector', headerShown: true }}>
            {() => <FaceDetector isAuthenticated={isAuthenticated} />}
        </Stack.Screen>
    </Stack.Navigator>
  );
};

export default ProfileStack;
