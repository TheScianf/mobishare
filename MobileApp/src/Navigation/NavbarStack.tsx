import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import Icon from 'react-native-vector-icons/Ionicons';
import HomeStack from './HomeStack';
import ProfileStack from './ProfileStack';
import CorsaStack from './CorsaStack';
import WalletStack from './WalletStack.tsx';

const Tab = createBottomTabNavigator();

const NavbarStack = () => {
  return (
    <Tab.Navigator
      screenOptions={({route}) => ({
        headerShown: false,
        tabBarIcon: ({color, size}) => {
          let iconName = 'car-outline';

          if (route.name === 'Home') iconName = 'home-outline';
          else if (route.name === 'Wallet') iconName = 'wallet-outline';
          else if (route.name === 'Profile') iconName = 'person-outline';
          else if (route.name === 'ScanQR') iconName = 'qr-code-outline';

          return <Icon name={iconName} size={size} color={color} />;
        },
        tabBarActiveTintColor: '#007AFF',
        tabBarInactiveTintColor: '#444',
        tabBarStyle: {
          backgroundColor: '#fff', // se vuoi cambiare anche il colore della navbar in basso
        },
      })}>
      <Tab.Screen name="Home" component={HomeStack} />
      <Tab.Screen name="ScanQR" component={CorsaStack} />
      <Tab.Screen name="Wallet" component={WalletStack} />
      <Tab.Screen name="Profile" component={ProfileStack} />
    </Tab.Navigator>
  );
};

export default NavbarStack;
