import React, {useEffect, useRef, useState} from 'react';
import {
  ActivityIndicator,
  Alert,
  Animated,
  Easing,
  StatusBar,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import {ImagePickerResponse, launchCamera} from 'react-native-image-picker';
import FaceRecognition from '../../Requests/FaceRecognition.ts';
import {styles} from '../../StyleSheets/faceSignIn.tsx';
import {useNavigation} from '@react-navigation/native';
import {AuthEntity, useAuth} from '../../Contexts/AuthContext.tsx';

const ModernFaceCamera = (props?: {loginButton: boolean}) => {
  const [loading, setLoading] = useState(false);
  const [, setStep] = useState(0); // 0: ready, 1: processing, 2: success/error
  const {isAuthenticated, setIsAuthenticated} = useAuth();

  // Animazioni
  const scaleAnim = useRef(new Animated.Value(1)).current;
  const fadeAnim = useRef(new Animated.Value(1)).current;
  const slideAnim = useRef(new Animated.Value(0)).current;
  const pulseAnim = useRef(new Animated.Value(1)).current;
  const rotateAnim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    // Animazione iniziale di entrata
    Animated.parallel([
      Animated.timing(slideAnim, {
        toValue: 1,
        duration: 800,
        useNativeDriver: true,
        easing: Easing.out(Easing.back(1.2)),
      }),
      Animated.timing(fadeAnim, {
        toValue: 1,
        duration: 600,
        useNativeDriver: true,
      }),
    ]).start();

    // Animazione pulsante continua

    const startPulseAnimation = () => {
      Animated.loop(
        Animated.sequence([
          Animated.timing(pulseAnim, {
            toValue: 1.05,
            duration: 2000,
            useNativeDriver: true,
            easing: Easing.inOut(Easing.sin),
          }),
          Animated.timing(pulseAnim, {
            toValue: 1,
            duration: 2000,
            useNativeDriver: true,
            easing: Easing.inOut(Easing.sin),
          }),
        ]),
      ).start();
    };

    startPulseAnimation();
  }, [fadeAnim, pulseAnim, slideAnim]);

  const startLoadingAnimation = () => {
    Animated.loop(
      Animated.timing(rotateAnim, {
        toValue: 1,
        duration: 2000,
        useNativeDriver: true,
        easing: Easing.linear,
      }),
    ).start();
  };

  const openCamera = () => {
    setLoading(true);
    setStep(1);
    startLoadingAnimation();

    // Animazione bottone durante il caricamento
    Animated.timing(scaleAnim, {
      toValue: 0.95,
      duration: 200,
      useNativeDriver: true,
    }).start();

    launchCamera(
      {
        mediaType: 'photo',
        includeBase64: false,
        cameraType: 'front',
        maxHeight: 1200,
        maxWidth: 1200,
        quality: 1,
        saveToPhotos: false,
      },
      async (response: ImagePickerResponse) => {
        if (response.didCancel || response.errorCode) {
          setLoading(false);
          setStep(0);
          Animated.timing(scaleAnim, {
            toValue: 1,
            duration: 200,
            useNativeDriver: true,
          }).start();
          return;
        }

        const asset = response.assets?.[0];
        if (!asset?.uri) {
          setLoading(false);
          setStep(0);
          Animated.timing(scaleAnim, {
            toValue: 1,
            duration: 200,
            useNativeDriver: true,
          }).start();
          return;
        }

        const photo = {
          uri: asset.uri,
          type: asset.type || 'image/jpeg',
          fileName: asset.fileName || 'photo.jpg',
        };

        try {
          const result = await FaceRecognition.faceSignIn(photo);
          setLoading(false);
          setStep(2);

          if (result) {
            console.log('Errore:', result.message);
            Alert.alert(
              '❌ Accesso Negato',
              result.message || 'Volto non riconosciuto. Riprova.',
            );
          } else {
            console.log('✅ Riconoscimento positivo');
            Alert.alert(
              '✅ Accesso Consentito',
              'Benvenuto! Volto riconosciuto con successo.',
            );
            setIsAuthenticated(
              new AuthEntity(
                isAuthenticated.username,
                isAuthenticated.token,
                isAuthenticated.role,
                new Date(),
              ),
            );
          }
        } catch (error) {
          setLoading(false);
          setStep(0);
          Alert.alert(
            '⚠️ Errore di Sistema',
            'Si è verificato un errore. Riprova più tardi.',
          );
        }

        // Reset animazioni
        setTimeout(() => {
          setStep(0);
          Animated.timing(scaleAnim, {
            toValue: 1,
            duration: 300,
            useNativeDriver: true,
          }).start();
        }, 2000);
      },
    );
  };

  const navigation: any = useNavigation();
  const openLogin = () => {
    navigation.navigate('SignIn');
  };

  const spin = rotateAnim.interpolate({
    inputRange: [0, 1],
    outputRange: ['0deg', '360deg'],
  });

  return (
    <>
      <StatusBar barStyle="light-content" backgroundColor="#0a0a0a" />
      <View style={styles.container}>
        {/* Header */}
        <Animated.View
          style={[
            styles.header,
            {
              opacity: fadeAnim,
              transform: [
                {
                  translateY: slideAnim.interpolate({
                    inputRange: [0, 1],
                    outputRange: [-50, 0],
                  }),
                },
              ],
            },
          ]}>
          <Text style={styles.title}>Riconoscimento Facciale</Text>
          <Text style={styles.subtitle}>Accesso Sicuro e Veloce</Text>
        </Animated.View>

        {/* Main Content */}
        <View style={styles.mainContent}>
          {/* Face Icon Circle */}
          <Animated.View
            style={[
              styles.faceContainer,
              {
                transform: [
                  {scale: pulseAnim},
                  {
                    translateY: slideAnim.interpolate({
                      inputRange: [0, 1],
                      outputRange: [30, 0],
                    }),
                  },
                ],
              },
            ]}>
            <View style={styles.faceBorder}>
              <View style={styles.faceInner}>
                {loading ? (
                  <Animated.View style={{transform: [{rotate: spin}]}}>
                    <Text style={styles.faceIcon}>🔄</Text>
                  </Animated.View>
                ) : (
                  <Text style={styles.faceIcon}>😊</Text>
                )}
              </View>
            </View>

            {/* Corner indicators */}
            <View style={[styles.corner, styles.topLeft]} />
            <View style={[styles.corner, styles.topRight]} />
            <View style={[styles.corner, styles.bottomLeft]} />
            <View style={[styles.corner, styles.bottomRight]} />
          </Animated.View>

          {/* Instructions */}
          <Animated.View
            style={[
              styles.instructionsContainer,
              {
                opacity: fadeAnim,
                transform: [
                  {
                    translateY: slideAnim.interpolate({
                      inputRange: [0, 1],
                      outputRange: [20, 0],
                    }),
                  },
                ],
              },
            ]}>
            <Text style={styles.instructionTitle}>Come procedere:</Text>
            <View style={styles.instructionItem}>
              <Text style={styles.instructionNumber}>1</Text>
              <Text style={styles.instructionText}>
                Posiziona il viso al centro del riquadro
              </Text>
            </View>
            <View style={styles.instructionItem}>
              <Text style={styles.instructionNumber}>2</Text>
              <Text style={styles.instructionText}>
                Assicurati di avere buona illuminazione
              </Text>
            </View>
            <View style={styles.instructionItem}>
              <Text style={styles.instructionNumber}>3</Text>
              <Text style={styles.instructionText}>
                Tocca il pulsante per scattare la foto
              </Text>
            </View>
          </Animated.View>
        </View>

        {/* Action Button */}
        <Animated.View
          style={[
            styles.buttonContainer,
            {
              opacity: fadeAnim,
              transform: [
                {scale: scaleAnim},
                {
                  translateY: slideAnim.interpolate({
                    inputRange: [0, 1],
                    outputRange: [50, 0],
                  }),
                },
              ],
            },
          ]}>
          <TouchableOpacity
            style={[styles.button, loading && styles.buttonDisabled]}
            onPress={openCamera}
            disabled={loading}
            activeOpacity={0.8}>
            <View style={styles.buttonContent}>
              {loading ? (
                <>
                  <ActivityIndicator
                    size="small"
                    color="#ffffff"
                    style={styles.buttonLoader}
                  />
                  <Text style={styles.buttonText}>Elaborazione...</Text>
                </>
              ) : (
                <>
                  <Text style={styles.buttonIcon}>📸</Text>
                  <Text style={styles.buttonText}>Inizia Riconoscimento</Text>
                </>
              )}
            </View>
          </TouchableOpacity>
          {props?.loginButton && (
            <TouchableOpacity
              onPress={openLogin}
              disabled={loading}
              activeOpacity={0.8}>
              <Text style={styles.buttonText}>oppure effettua il login</Text>
            </TouchableOpacity>
          )}

          <Text style={styles.securityNote}>
            🔒 I tuoi dati biometrici sono protetti e criptati
          </Text>
        </Animated.View>

        {/* Loading Overlay */}
        {loading && (
          <Animated.View style={[styles.loadingOverlay, {opacity: fadeAnim}]}>
            <View style={styles.loadingContent}>
              <Animated.View style={{transform: [{rotate: spin}]}}>
                <View style={styles.loadingCircle}>
                  <ActivityIndicator size="large" color="#00d4ff" />
                </View>
              </Animated.View>
              <Text style={styles.loadingTitle}>Analisi in corso</Text>
              <Text style={styles.loadingSubtext}>
                Elaborazione dei dati biometrici...
              </Text>

              <View style={styles.progressContainer}>
                <View style={styles.progressBar}>
                  <Animated.View
                    style={[
                      styles.progressFill,
                      // {
                      //   width: pulseAnim.interpolate({
                      //     inputRange: [1, 1.05],
                      //     outputRange: ['60%', '90%'],
                      //   }),
                      // },
                    ]}
                  />
                </View>
              </View>
            </View>
          </Animated.View>
        )}
      </View>
    </>
  );
};

export default ModernFaceCamera;
