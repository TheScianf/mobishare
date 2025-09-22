import React, {useEffect, useState} from 'react';
import {
  Alert,
  Modal,
  ScrollView,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {useNavigation} from '@react-navigation/native';
import {AuthEntity, useAuth} from '../../Contexts/AuthContext';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {styles} from '../../StyleSheets/profileStyle.tsx';

const Profile = () => {
  const {isAuthenticated, setIsAuthenticated} = useAuth();
  const navigation: any = useNavigation();
  const [showFaceModal, setShowFaceModal] = useState(false);
  const [faceAuthChoice, setFaceAuthChoice] = useState<string | null>(null);

  useEffect(() => {
    const loadChoice = async () => {
      try {
        const choice = await AsyncStorage.getItem('faceAuthChoice');
        setFaceAuthChoice(choice);
      } catch (error) {
        console.error('Errore nel caricamento faceAuthChoice:', error);
      }
    };

    loadChoice();
  }, []);

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
            );
          },
        },
      ],
      {cancelable: true},
    );
  };

  const setChoice = async (value: string) => {
    await AsyncStorage.setItem('faceAuthChoice', value);
    setFaceAuthChoice(value); // <-- aggiorna lo stato locale!
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Il mio profilo</Text>

      <ScrollView
        contentContainerStyle={styles.menu}
        showsVerticalScrollIndicator={false}>
        <TouchableOpacity
          style={styles.menuItem}
          onPress={() => navigation.navigate('PersonalInfoScreen')}>
          <View style={styles.menuItemLeft}>
            <Icon
              name="person-outline"
              size={24}
              color="#4B0055"
              style={styles.menuIcon}
            />
            <Text style={styles.menuItemText}>Informazioni Personali</Text>
          </View>
          <Icon name="chevron-forward" size={20} color="#aaa" />
        </TouchableOpacity>

        {/* Pulsante per attivare il riconoscimento facciale */}
        {}
        {/* Mostra solo se faceAuthChoice è null, vuoto o "no" */}
        {(faceAuthChoice === null ||
          faceAuthChoice === '' ||
          faceAuthChoice === 'no') && (
          <TouchableOpacity
            style={styles.menuItem}
            onPress={() => setShowFaceModal(true)}>
            <View style={styles.menuItemLeft}>
              <Icon
                name="camera-outline"
                size={24}
                color="#4B0055"
                style={styles.menuIcon}
              />
              <Text style={styles.menuItemText}>
                Attiva riconoscimento facciale
              </Text>
            </View>
            <Icon name="chevron-forward" size={20} color="#aaa" />
          </TouchableOpacity>
        )}
        <TouchableOpacity
          style={styles.menuItem}
          onPress={() =>
            setIsAuthenticated(
              new AuthEntity(
                isAuthenticated.username,
                isAuthenticated.token,
                isAuthenticated.role,
                new Date(0),
              ),
            )
          }>
          <View style={styles.menuItemLeft}>
            <Icon
              name="camera-outline"
              size={24}
              color="#4B0055"
              style={styles.menuIcon}
            />
            <Text style={styles.menuItemText}>expire face login</Text>
          </View>
          <Icon name="chevron-forward" size={20} color="#aaa" />
        </TouchableOpacity>

        <TouchableOpacity style={styles.menuItem} onPress={() => setChoice('')}>
          <View style={styles.menuItemLeft}>
            <Icon
              name="wallet-outline"
              size={24}
              color="#4B0055"
              style={styles.menuIcon}
            />
            <Text style={styles.menuItemText}>
              Resetta scelta riguardo il riconoscimento facciale (for testing)
            </Text>
          </View>
          <Icon name="chevron-forward" size={20} color="#aaa" />
        </TouchableOpacity>

        <TouchableOpacity style={styles.logout} onPress={handleLogout}>
          <Icon
            name="log-out-outline"
            size={22}
            color="#D10055"
            style={styles.logoutIcon}
          />
          <Text style={styles.logoutText}>Esci</Text>
        </TouchableOpacity>
      </ScrollView>

      {/* Modal per attivazione face ID */}
      <Modal visible={showFaceModal} transparent animationType="fade">
        <View style={styles.modalOverlay}>
          <View style={styles.modalContainer}>
            <Text style={styles.modalTitle}>
              Vuoi attivare il riconoscimento facciale?
            </Text>
            <View style={styles.modalButtons}>
              <TouchableOpacity
                onPress={() => {
                  setShowFaceModal(false);
                  setChoice('si');
                  navigation.navigate('RiconoscimentoFacciale');
                }}
                style={styles.modalButton}>
                <Text style={styles.modalButtonText}>Sì</Text>
              </TouchableOpacity>
              <TouchableOpacity
                onPress={() => setShowFaceModal(false)}
                style={[styles.modalButton, styles.modalCancel]}>
                <Text style={styles.modalButtonText}>No</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>
    </View>
  );
};

export default Profile;
