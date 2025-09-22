import React from 'react';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import SuspendedUsersScreen from '../Screens/Manager/ListaUtentiSospesi.tsx';
import GestoreParcheggio from '../Screens/Manager/PaginaMezziManutenzione.tsx';
import HomeManager from '../Screens/Manager/HomeManager.tsx';
import ReactivateSuspendedUsers from '../Screens/Manager/RiattivaUtentiSospesi.tsx';
import ShowParks from '../Screens/Manager/ShowParks.tsx';
import {ManageManager} from '../Screens/Manager/ManageManager.tsx';
import ShowParksManager from '../Screens/Manager/ShowParksRestricted.tsx';

const Stack = createNativeStackNavigator();

const ProfileStack = () => {

    return (
        <Stack.Navigator initialRouteName="HomeManager">
            <Stack.Screen
                name="HomeManager"
                component={HomeManager}
                options={{ headerShown: false }}
            />
            <Stack.Screen
                name="SuspendedUsersScreen"
                component={SuspendedUsersScreen}
                options={{ title: 'Gestione sospensione' }}
            />
            <Stack.Screen
                name="MezziScreen"
                component={GestoreParcheggio}
                options={{ title: 'Mezzi' }}
            />
            <Stack.Screen
                name="ParksScreen"
                component={ShowParks}
                options={{ title: 'Parcheggi' }}>
            </Stack.Screen>
            <Stack.Screen
                name="ReactivateSuspendedUsersScreen"
                component={ReactivateSuspendedUsers}
                options={{ title: 'Riattiva account sospeso' }}
            />
          <Stack.Screen
                name="ManageManagersScreen"
                component={ManageManager}
                options={{ title: 'Gestione Manager' }}
            />
            <Stack.Screen
                name="ParksScreenManager"
                component={ShowParksManager}
                options={{ title: 'Gestione parcheggi singolo manager' }}
            />
        </Stack.Navigator>
    );
};

export default ProfileStack;
