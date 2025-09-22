import React from 'react';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import LoginScreen from '../Screens/SignUp-In/SignIn';
import SignUpScreen from '../Screens/SignUp-In/SignUp';
import ChiSiamoScreen from '../Screens/Info/ChiSiamo';
import TermsOfServiceScreen from '../Screens/Info/TermsOfService';
import PrivacyPolicyScreen from '../Screens/Info/PrivacyPolicy';
import {useAuth} from '../Contexts/AuthContext';
import AccountSuspendedScreen from '../Screens/Home/AccountSospeso.tsx';
import ModernFaceCameraScreen from '../Screens/SignUp-In/FaceSignIn.tsx';

const Stack = createNativeStackNavigator();

const AuthStack = () => {
  const {setIsAuthenticated} = useAuth();

  return (
    <Stack.Navigator screenOptions={{headerShown: false}}>
      <Stack.Screen name="SignIn">
        {() => <LoginScreen setIsAuthenticated={setIsAuthenticated} />}
      </Stack.Screen>
      <Stack.Screen name="SignUp">
        {() => <SignUpScreen setIsAuthenticated={setIsAuthenticated} />}
      </Stack.Screen>
      <Stack.Screen
        name="ChiSiamo"
        component={ChiSiamoScreen}
        options={{headerShown: true, title: 'Chi siamo'}}
      />
      <Stack.Screen
        name="TermsOfService"
        component={TermsOfServiceScreen}
        options={{headerShown: true, title: 'Termini di servizio'}}
      />
      <Stack.Screen
        name="PrivacyPolicy"
        component={PrivacyPolicyScreen}
        options={{headerShown: true, title: 'Privacy and Policy'}}
      />
      <Stack.Screen
        name="AccountSospesoScreen"
        component={AccountSuspendedScreen}
        options={{headerShown: true, title: 'Account sospeso'}}
      />
      <Stack.Screen
        name="RealTimeFaceAccessScreen"
        component={() => ModernFaceCameraScreen()}
        options={{title: 'Face Detector', headerShown: true}}
      />
    </Stack.Navigator>
  );
};

export default AuthStack;
