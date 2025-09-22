import React, {useState} from 'react';
import {
  SafeAreaView,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import {useNavigation} from '@react-navigation/native';
import {AuthEntity, useAuth} from '../../Contexts/AuthContext.tsx';
import authRequests from '../../Requests/AuthRequests.ts';
import {LoginError} from '../../Requests/Utils.ts';
import {styles} from '../../StyleSheets/signInStyle.tsx';

const LoginScreen = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const {setIsAuthenticated} = useAuth();
  const navigation: any = useNavigation();

  const handleContinue = async () => {
    try {
      console.log(username, password);

      const result = await authRequests.login(username, password);
      if (result instanceof AuthEntity) {
        setIsAuthenticated(result);
      } else {
        setError((result as LoginError).message);
      }
    } catch (error) {
      console.error('Errore durante il login:', error);
      setError('Si è verificato un errore durante il login');
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.appName}>MobiShare</Text>

      <View style={styles.formContainer}>
        <View style={styles.headerSection}>
          <Text style={styles.title}>Bentornato</Text>
          <Text style={styles.subtitle}>
            Inserisci il tuo username per continuare
          </Text>
        </View>

        <View style={styles.inputSection}>
          <TextInput
            style={styles.input}
            placeholder="Username"
            placeholderTextColor="#888"
            keyboardType="email-address"
            autoCapitalize="none"
            value={username}
            onChangeText={setUsername}
          />

          <TextInput
            style={styles.input}
            placeholder="Inserisci la tua password"
            placeholderTextColor="#888"
            secureTextEntry
            autoCapitalize="none"
            value={password}
            onChangeText={setPassword}
          />
        </View>

        {error !== '' && (
          <View style={styles.errorContainer}>
            <Text style={styles.error}>{error}</Text>
          </View>
        )}

        <View style={styles.buttonSection}>
          <TouchableOpacity
            style={styles.button}
            onPress={handleContinue}
            activeOpacity={0.8}>
            <Text style={styles.buttonText}>Continua</Text>
          </TouchableOpacity>

          {/*<Text style={styles.orText}>Oppure</Text>*/}

          {/*<TouchableOpacity*/}
          {/*  style={[styles.button, styles.outlineButton]}*/}
          {/*  onPress={() => navigation.navigate('RealTimeFaceAccessScreen')}*/}
          {/*  activeOpacity={0.8}>*/}
          {/*  <Text style={[styles.buttonText, styles.outlineButtonText]}>*/}
          {/*    Accedi con il volto*/}
          {/*  </Text>*/}
          {/*</TouchableOpacity>*/}
        </View>

        <TouchableOpacity
          onPress={() => navigation.navigate('SignUp')}
          style={styles.signupLink}>
          <Text style={styles.signupText}>
            Non hai un account? <Text style={styles.link}>Registrati</Text>
          </Text>
        </TouchableOpacity>
      </View>

      <View style={styles.legalTextContainer}>
        <Text style={styles.legalText}>
          Cliccando su "Continua", accetti i nostri{' '}
          <Text
            style={styles.link}
            onPress={() => navigation.navigate('TermsOfService')}>
            Termini di servizio
          </Text>{' '}
          e la nostra{' '}
          <Text
            style={styles.link}
            onPress={() => navigation.navigate('PrivacyPolicy')}>
            Privacy Policy
          </Text>
          .
        </Text>
      </View>
    </SafeAreaView>
  );
};

export default LoginScreen;
