import React from 'react';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import Wallet from '../Screens/Wallet/Wallet';
import WalletHistory from '../Screens/Wallet/WalletHistory';
import RechargeAmountScreen from '../Screens/Wallet/RechargeAmount';

const Stack = createNativeStackNavigator();

const WalletStack = () => {

  return (
    <Stack.Navigator initialRouteName="WalletMain">
      <Stack.Screen
        name="WalletMain"
        component={Wallet}
        options={{headerShown: false}}
      />
      <Stack.Screen
        name="WalletHistoryScreen"
        options={{title: 'Cronologia borsellino'}}
        component={WalletHistory}
      >
      </Stack.Screen>
      <Stack.Screen
        name="RechargeAmountScreen"
        component={RechargeAmountScreen}
        options={{title: 'Seleziona importo'}}
      />
    </Stack.Navigator>
  );
};

export default WalletStack;
