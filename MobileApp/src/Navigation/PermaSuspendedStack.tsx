import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import AccountPermaSuspendedScreen from '../Screens/Home/AccountPermaSuspended.tsx';


const Stack = createNativeStackNavigator();

const PermaSuspendedStack = () => {

    return (
        <Stack.Navigator screenOptions={{ headerShown: false }}>
            <Stack.Screen
                name="AccountSospeso"
                component={AccountPermaSuspendedScreen}
                options={{ headerShown: false }}
            />
        </Stack.Navigator>
    );
};

export default PermaSuspendedStack;
