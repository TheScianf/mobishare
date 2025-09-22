import React, {useEffect, useState} from 'react';
import {
  Alert,
  ScrollView,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {useNavigation} from '@react-navigation/native';
import {AuthEntity, useAuth} from '../../Contexts/AuthContext';
import {isAdmin} from '../../Requests/Manager.ts'; // Percorso corretto al contesto di autenticazione
import {styles} from '../../StyleSheets/homeManagerStyle.tsx';

const HomeManager = () => {
  const {setIsAuthenticated} = useAuth(); // ✅ prendi dal contesto

  const navigation: any = useNavigation();
  const {isAuthenticated} = useAuth();

  const handleLogout = () => {
    Alert.alert(
      'Sei sicuro?',
      'Vuoi davvero uscire dall’account?',
      [
        {text: 'Annulla', style: 'cancel'},
        {
          text: 'Esci',
          style: 'destructive',
          onPress: () => {
            setIsAuthenticated(
              new AuthEntity(undefined, undefined, AuthEntity.ANONYMOUS),
            ); // logout solo dopo conferma
          },
        },
      ],
      {cancelable: true},
    );
  };

  interface MenuItem {
    id: string;
    label: string;
    icon: string;
    route: string;
  }

  const [role, setRole] = useState<Number | undefined>(undefined);
  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);

  useEffect(() => {
    isAdmin(isAuthenticated.token as string).then(isAdmin => {
      if (isAdmin) {
        setRole(AuthEntity.ADMIN);
      } else {
        setRole(AuthEntity.MANAGER);
      }
    });
  }, [isAuthenticated.token]);

  useEffect(() => {
    const list = [
      //shared pages to both Manager and Admin
      {id: '8', label: 'Manutenzioni', icon: 'person', route: 'MezziScreen'},
    ];
    if (role === AuthEntity.MANAGER) {
      //Pages for Manager only (if exists)
      list.push(
          {id: '5', label: 'Parcheggi', icon: 'car', route: 'ParksScreenManager'},
      );
    }
    if (role === AuthEntity.ADMIN) {
      //Pages for Admin only
      list.push(
          {id: '5', label: 'Parcheggi', icon: 'car', route: 'ParksScreen'},
          {
          id: '2',
          label: 'Utenti sospesi',
          icon: 'person',
          route: 'SuspendedUsersScreen',
        },
        {
          id: '6',
          label: 'Riattiva utenti sospesi',
          icon: 'person',
          route: 'ReactivateSuspendedUsersScreen',
        },
        {
          id: '7',
          label: 'Gestione Manager',
          icon: 'person',
          route: 'ManageManagersScreen',
        }
      );
    }
    setMenuItems(list);
  }, [role]);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Gestisci MobiShare</Text>
      <ScrollView
        contentContainerStyle={styles.menu}
        showsVerticalScrollIndicator={false}>
        {menuItems.map(item => (
          <TouchableOpacity
            key={item.id}
            style={styles.menuItem}
            onPress={() => navigation.navigate(item.route)}
            accessibilityLabel={item.label}
            accessibilityRole="button"
            activeOpacity={0.7}>
            <View style={styles.menuItemLeft}>
              <Icon
                name={item.icon}
                size={24}
                color="#4B0055"
                style={styles.menuIcon}
              />
              <Text style={styles.menuItemText}>{item.label}</Text>
            </View>
            <Icon name="chevron-forward" size={20} color="#aaa" />
          </TouchableOpacity>
        ))}

        <TouchableOpacity
          style={styles.logout}
          onPress={handleLogout}
          accessibilityLabel="Esci dall'account"
          accessibilityRole="button"
          activeOpacity={0.7}>
          <Icon
            name="log-out-outline"
            size={22}
            color="#D10055"
            style={styles.logoutIcon}
          />
          <Text style={styles.logoutText}>Esci</Text>
        </TouchableOpacity>
      </ScrollView>
    </View>
  );
};

export default HomeManager;
