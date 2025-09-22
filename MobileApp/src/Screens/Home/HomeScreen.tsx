import React, { useState, useEffect } from 'react';
import {Image, SafeAreaView, View, Text, TouchableOpacity } from 'react-native';
import MapView, { Marker } from 'react-native-maps';
import SearchBar from './SearchBar';
import ParkingModal from './ParkingModal';
import { useNavigation } from '@react-navigation/native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import HomeRequest, { park } from '../../Requests/HomeRequest';
import {useAuth} from '../../Contexts/AuthContext.tsx';
import {styles} from '../../StyleSheets/homeScreenStyle.tsx';

const HomeScreen = () => {
  const { isAuthenticated} = useAuth();
  const navigation: any = useNavigation();
  const [selectedParking, setSelectedParking] = useState<park | null>(null);
  const [showFaceModal, setShowFaceModal] = useState(false);
  const [parks, setParks] = useState<park[]>([]);
  const [searchText, setSearchText] = useState('');


  useEffect(() => {
    const load = async () => {
      const faceChoice = await AsyncStorage.getItem('faceAuthChoice');
      if (!faceChoice) {
        setShowFaceModal(true);
      }
      const response = await HomeRequest.getParks(isAuthenticated.token as string);
      if (response instanceof Error) {
        console.log('Errore nella richiesta:', response.message);
      } else {
        console.log(response);
        setParks(response); // ParkInfo[]
      }
    };

    load();
  }, [isAuthenticated.token]);

  const filteredParks = parks.filter(p =>
      p.name.toLowerCase().includes(searchText.toLowerCase())
  );

  const saveFaceChoice = async (value: 'yes' | 'no') => {
    await AsyncStorage.setItem('faceAuthChoice', value);
    setShowFaceModal(false);
    if (value === 'yes') {
      navigation.navigate('RiconoscimentoFacciale');
    }
  };

  const openModal = (parking: park) => setSelectedParking(parking);
  const closeModal = () => setSelectedParking(null);

  console.log('Parchi:', parks);

  return (
      <SafeAreaView style={{ flex: 1 }}>
        <View style={styles.container}>
          <SearchBar value={searchText} onChangeText={setSearchText} />


          <MapView
              style={styles.map}
              initialRegion={{
                latitude: 45.32,
                longitude: 8.42,
                latitudeDelta: 0.01,
                longitudeDelta: 0.01,
              }}
          >
            {filteredParks.map((p) => {
              return (
                  <Marker
                      key={p.id}
                      coordinate={{
                        latitude: parseFloat(p.latitude),
                        longitude: parseFloat(p.longitude),
                      }}
                      onPress={() => openModal(p)}
                  >
                    <Image
                        source={require('../../../assets/parcheggio.png')}
                        style={{ width: 40, height: 40 }}
                        resizeMode="contain"
                    />
                  </Marker>
              );
            })}
          </MapView>

          <ParkingModal
              visible={selectedParking !== null}
              parking={selectedParking}
              onClose={closeModal}
          />
        </View>

        {showFaceModal && (
            <View style={styles.modalOverlay}>
              <View style={styles.modalContainer}>
                <Text style={styles.modalTitle}>Vuoi attivare il riconoscimento facciale?</Text>
                <View style={styles.modalButtons}>
                  <TouchableOpacity onPress={() => saveFaceChoice('yes')} style={styles.modalButton}>
                    <Text style={styles.modalButtonText}>Sì</Text>
                  </TouchableOpacity>
                  <TouchableOpacity onPress={() => saveFaceChoice('no')} style={[styles.modalButton, styles.modalCancel]}>
                    <Text style={styles.modalButtonText}>No</Text>
                  </TouchableOpacity>
                </View>
              </View>
            </View>
        )}
      </SafeAreaView>
  );
};

export default HomeScreen;

