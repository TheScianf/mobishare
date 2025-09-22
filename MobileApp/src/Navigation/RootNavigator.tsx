import React, {useEffect, useState} from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {AuthEntity, useAuth} from '../Contexts/AuthContext';
import AuthStack from './AuthStack'; // Login, SignIn, ecc.
import NavbarStack from './NavbarStack'; // NavbarStack o simili
import ManagerStack from './ManagerStack';
import SuspendedStack from './SuspendedStack.tsx';
import PermaSuspendedStack from './PermaSuspendedStack';
import {Text} from 'react-native';
import {isP_Suspended, isSuspended} from '../Requests/Customer';
import ModernFaceCamera from '../Screens/SignUp-In/FaceSignIn.tsx';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import SignIn from '../Screens/SignUp-In/SignIn.tsx';

const RootNavigator = () => {
  const {isAuthenticated} = useAuth();
  const [role, setRole] = useState<Number | undefined>(undefined);

  useEffect(() => {
    setRole(isAuthenticated.role);
    if (isAuthenticated.role === AuthEntity.CUSTOMER) {
      isSuspended(isAuthenticated.token as string).then(suspended => {
        if (suspended) {
          setRole(AuthEntity.SUSPENDED);
        }
      });
      isP_Suspended(isAuthenticated.token as string).then(pSuspended => {
        if (pSuspended) {
          setRole(AuthEntity.P_SUSPENDED);
        }
      });
    }
  }, [isAuthenticated.role, isAuthenticated.token]);

  return (
    <NavigationContainer>
      {(() => {
        switch (role) {
          case AuthEntity.MANAGER:
          case AuthEntity.ADMIN:
            return <ManagerStack />;
          case AuthEntity.CUSTOMER:
            //se facelogin
            if (isAuthenticated.faceLoginDate === false) {
              //se scaduta la rinnova
              const Stack = createNativeStackNavigator();
              const Component = () => <ModernFaceCamera loginButton={true} />;
              return (
                <Stack.Navigator screenOptions={{headerShown: false}}>
                  <Stack.Screen
                    name="ModernFaceCameraScreen"
                    component={Component}
                  />
                  <Stack.Screen
                    name="SignIn"
                    component={SignIn}
                    options={{headerShown: true, title: 'Sign In'}}
                  />
                </Stack.Navigator>
              );
            }
            //se disabilitata o Date (quindi valida perchè non è stata cambiata da authContext)
            return <NavbarStack />;
          case AuthEntity.ANONYMOUS:
            return <AuthStack />;
          case AuthEntity.SUSPENDED:
            return <SuspendedStack />;
          case AuthEntity.P_SUSPENDED:
            return <PermaSuspendedStack />;
          default:
            return <Text style={{fontSize: 100}}>Loading...</Text>;
        }
      })()}
    </NavigationContainer>
  );
};
export default RootNavigator;
