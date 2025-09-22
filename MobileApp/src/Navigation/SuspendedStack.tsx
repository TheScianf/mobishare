import React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import AccountSuspendedScreen from '../Screens/Home/AccountSospeso.tsx';
import RechargeWalletReactivation from '../Screens/Home/RechargeWalletReactivation.tsx';

const Stack = createNativeStackNavigator();

const SuspendedStack = () => {

    return (
        <Stack.Navigator screenOptions={{ headerShown: false }}>
            <Stack.Screen
                name="AccountSospeso"
                component={AccountSuspendedScreen}
                options={{ headerShown: false }}
            />
            <Stack.Screen
                name="RechargeWallet"
                component={RechargeWalletReactivation}
                options={{ headerShown: true }}
            />
        </Stack.Navigator>
    );
};

export default SuspendedStack;
