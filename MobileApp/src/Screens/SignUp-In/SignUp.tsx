import React, { useState } from 'react';
import { View, Text, TextInput, TouchableOpacity,SafeAreaView, ScrollView } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import {AuthEntity, useAuth} from '../../Contexts/AuthContext.tsx';
import authRequests from '../../Requests/AuthRequests.ts';
import {styles} from '../../StyleSheets/signUpStyle.tsx';

const SignUpScreen = () => {
  const navigation: any = useNavigation();
  const {setIsAuthenticated} = useAuth();

  const [firstName, setFirstName] = useState('');
  const [surname, setSurname] = useState('');
  const [username, setUsername] = useState('');
  const [gender, setGender] = useState('');
  const [cf, setCf] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState<{ [key: string]: string }>({});
  const [error, setError] = useState('');

  const validateEmail = (email: string): boolean =>
      /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

  const validateCF = (cf: string): boolean =>
      /^[A-Z0-9]{16}$/i.test(cf);

  const validateGender = (gender: string): boolean =>
      /^(M|F|X)$/i.test(gender);

  const handleValidation = (field: string, value: string) => {
    let newErrors = { ...errors };

    switch (field) {
      case 'email':
        if (!validateEmail(value)) {newErrors.email = 'Email non valida';}
        else {delete newErrors.email;}
        break;
      case 'cf':
        if (!validateCF(value)) {newErrors.cf = 'Codice Fiscale non valido';}
        else {delete newErrors.cf;}
        break;
      case 'gender':
        if (!validateGender(value)) {newErrors.gender = 'Usa M, F o X';}
        else {delete newErrors.gender;}
        break;
      case 'password':
        if (value.length < 6) {newErrors.password = 'Minimo 6 caratteri';}
        else {delete newErrors.password;}
        break;
      case 'firstName':
        if (!value.trim() || value.length < 4) {newErrors.firstName = 'Nome obbligatorio di almeno 4 caratteri';}
        else {delete newErrors.firstName;}
        break;
      case 'surname':
        if (!value.trim() || value.length < 4) {newErrors.surname = 'Cognome obbligatorio di almeno 4 caratteri';}
        else {delete newErrors.surname;}
        break;
      case 'username':
        if (!value.trim()) {newErrors.username = 'Username obbligatorio di almeno 4 caratteri';}
        else {delete newErrors.username;}
        break;
      default:
        break;
    }

    setErrors(newErrors);
  };

  const handleSignUp = async () => {
    const result = await authRequests.register(
        username,
        password,
        firstName,
        surname,
        gender,
        cf,
        email,
    );

    if (result === undefined) {
      const resultLogin = await authRequests.login(username, password);

      if (resultLogin instanceof AuthEntity) {
        setIsAuthenticated(resultLogin);
      } else {
        setError(resultLogin.message);
      }
    } else {
      setError(result.message);
    }
  };

  const renderInputField = (
      value: string,
      onChangeText: (text: string) => void,
      placeholder: string,
      fieldName: string,
      props = {}
  ) => (
      <View style={styles.inputContainer}>
        <TextInput
            style={[styles.input, errors[fieldName] && styles.errorInput]}
            placeholder={placeholder}
            placeholderTextColor="#888"
            value={value}
            onChangeText={(text) => {
              onChangeText(text);
              handleValidation(fieldName, text);
            }}
            {...props}
        />
        {errors[fieldName] && (
            <Text style={styles.errorText}>{errors[fieldName]}</Text>
        )}
      </View>
  );

  return (
      <SafeAreaView style={styles.container}>
        <ScrollView showsVerticalScrollIndicator={false} contentContainerStyle={styles.scrollContent}>
          <Text style={styles.appName}>MobiShare</Text>

          <View style={styles.formContainer}>
            <View style={styles.headerSection}>
              <Text style={styles.title}>Crea il tuo account</Text>
              <Text style={styles.subtitle}>Inserisci i tuoi dati per iniziare</Text>
            </View>

            <View style={styles.inputSection}>
              {renderInputField(firstName, setFirstName, 'Nome', 'firstName')}
              {renderInputField(surname, setSurname, 'Cognome', 'surname')}
              {renderInputField(username, setUsername, 'Username', 'username')}
              {renderInputField(cf, setCf, 'Codice Fiscale', 'cf')}
              {renderInputField(gender, setGender, 'Sesso (M/F/X)', 'gender')}
              {renderInputField(
                  email,
                  setEmail,
                  'email@dominio.com',
                  'email',
                  { keyboardType: 'email-address', autoCapitalize: 'none' }
              )}
              {renderInputField(
                  password,
                  setPassword,
                  'Password',
                  'password',
                  { secureTextEntry: true }
              )}
            </View>

            {error !== '' && (
                <View style={styles.errorContainer}>
                  <Text style={styles.error}>{error}</Text>
                </View>
            )}

            <View style={styles.buttonSection}>
              <TouchableOpacity style={styles.button} onPress={handleSignUp}>
                <Text style={styles.buttonText}>Registrati</Text>
              </TouchableOpacity>

              <TouchableOpacity
                  onPress={() => navigation.navigate('SignIn')}
                  style={styles.loginLink}>
                <Text style={styles.loginText}>
                  Hai già un account? <Text style={styles.link}>Accedi</Text>
                </Text>
              </TouchableOpacity>
            </View>
          </View>

          <View style={styles.legalTextContainer}>
            <Text style={styles.legalText}>
              Cliccando su "Registrati", accetti i nostri{' '}
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
            </Text>
          </View>
        </ScrollView>
      </SafeAreaView>
  );
};

export default SignUpScreen;
