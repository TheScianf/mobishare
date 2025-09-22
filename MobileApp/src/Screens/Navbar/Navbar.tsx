import React from 'react';
import { View, TouchableOpacity } from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import { useNavigation } from '@react-navigation/native';
import { useRoute } from '@react-navigation/native';
import {styles} from '../../StyleSheets/navbarStyle.tsx';

const Navbar = () => {
  const navigation: any = useNavigation();
  const route = useRoute();

  const getIconColor = (name: string) =>
    route.name === name ? '#007AFF' : '#444';

  return (
    <View style={styles.bottomNav}>
      <TouchableOpacity onPress={() => navigation.navigate('Home')}>
        <Icon name="home" size={24} color={getIconColor('Home')} />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => navigation.navigate('ScanQR')}>
        <Icon name="qrcode-outline" size={24} color={getIconColor('ScanQR')} />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => navigation.navigate('Wallet')}>
        <Icon name="wallet-outline" size={24} color={getIconColor('Wallet')} />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => navigation.navigate('Profile')}>
        <Icon name="person-outline" size={24} color={getIconColor('Profile')} />
      </TouchableOpacity>
    </View>
  );
};


export default Navbar;
