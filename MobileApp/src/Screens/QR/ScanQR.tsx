import React, {useEffect, useState} from 'react';
import {Alert, Platform, SafeAreaView, StatusBar, Text, View} from 'react-native';
import {Camera} from 'react-native-camera-kit';
import {useNavigation} from '@react-navigation/native';
import RaceRequest from '../../Requests/RaceRequest';
import {useAuth} from '../../Contexts/AuthContext.tsx';
import {styles} from '../../StyleSheets/scanQRStyle.tsx';


const ScanQR = () => {
  const navigation: any = useNavigation();
  const [isScanActive, setIsScanActive] = useState(true);
  const [statusBarHeight] = useState(Platform.OS === 'ios' ? 20 : StatusBar.currentHeight || 0);
  const { isAuthenticated} = useAuth();


  useEffect(() => {
    return navigation.addListener('focus', () => {
      setIsScanActive(true);
    });
  }, [navigation]);

  const onBarcodeScan = async (event: any) => {
    if (!isScanActive) return;

    const scannedValue = event.nativeEvent?.codeStringValue;
    if (!scannedValue) return;

    setIsScanActive(false);

    const vehicleId = Number(scannedValue);

    try {
      const result = await RaceRequest.checkQR(
          isAuthenticated.username as string,
          vehicleId as number,
          isAuthenticated.token as string
      );

      // Gestione errori personalizzati dal server
      if (result instanceof Error) {
        // result è CheckQRError
        Alert.alert(
            'Errore',
            result.message,
            [{ text: 'OK', onPress: () => setIsScanActive(true) }]
        );
        return;
      }


      // Se arrivo qui, result è un raceVehicle valido
      navigation.navigate('CorsaStartScreen', { vehicleData: result });

    } catch (error: any) {
      console.error('Errore in checkQR:', error);
      Alert.alert(
          'Errore',
          error.message || 'Errore sconosciuto',
          [{ text: 'OK', onPress: () => setIsScanActive(true) }]
      );
    }
  };

  return (
      <SafeAreaView style={styles.container}>
        <StatusBar translucent backgroundColor="transparent" barStyle="dark-content" />
        {isScanActive && (
            <Camera
                style={styles.fullScreen}
                scanBarcode={true}
                onReadCode={onBarcodeScan}
                showFrame={true}
                laserColor="blue"
                frameColor="purple"
            />
        )}
        <View style={styles.overlayContainer}>
          <View style={[styles.headerContainer, { marginTop: statusBarHeight }]}>
            <Text style={styles.headerText}>Inquadra il QR Code</Text>
            <Text style={styles.subHeaderText}>Posiziona il codice all'interno dell'area</Text>
          </View>
        </View>
      </SafeAreaView>
  );
};


export default ScanQR;

